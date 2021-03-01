package com.SitStayCreate;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCMessageInfo;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OSCTranslator {

    //MIDI Device->External App
    public static OSCMessage translateMidiToOSC(MidiMessage midiMessage, Dimensions dimensions, String prefix) {
        int status = midiMessage.getStatus();

        byte[] midiMessageByteArray = midiMessage.getMessage();
        int midiData1 = midiMessageByteArray[1];
        int midiData2 = midiMessageByteArray[2];
        int width = dimensions.getWidth();
        int gridX = 0;
        int gridY = 0;
        int gridStatus = 0;
        //NoteOn message
        switch (status) {
            case 144:
                //convert midi to matrix
                gridX = midiData1 % width;
                gridY = (int) Math.floor(midiData1 / width);

                if(midiData2 > 0){
                    gridStatus = 1;
                }

                //invert after osc conversion
                if(dimensions.isInverted()){
                        //calculate the width of the grid

                        int maxValue = dimensions.getHeight() - 1;
                        gridY = maxValue - gridY;
                }
                break;

            case 128:
                //convert midi to matrix
                gridX = midiData1 % width;
                gridY = (int) Math.floor(midiData1 / width);

                if(dimensions.isInverted()){
                    //calculate the width of the grid
                    int maxValue = dimensions.getHeight() - 1;
                    gridY = maxValue - gridY;
                }
                break;
            }

        String addressString = String.format("%s/grid/key", prefix);
        //make this a list
        ArrayList oscArgs = new ArrayList();
        oscArgs.add(gridX);
        oscArgs.add(gridY);
        oscArgs.add(gridStatus);

        OSCMessageInfo oscMessageInfo = new OSCMessageInfo("iii");
        OSCMessage oscMessage = new OSCMessage(addressString, oscArgs, oscMessageInfo);

        return oscMessage;
    }

    //External App->MIDI Device
    //translates Led Messages (monobright)
    public static ShortMessage translateGridLedToMidi(int gridX, int gridY, int gridZ, Dimensions dimensions){
        //logic to get grid data to midi
        int status = 144; //noteOn message
        int noteVal;
        //calculate the width of the grid
        int gridWidth = dimensions.getWidth();
        int noteVel = 127;
        if(gridZ == 0){
            noteVel = 0;
        }

        if(dimensions.isInverted()){
            int maxValue = dimensions.getHeight() - 1;
            gridY = maxValue - gridY;
        }

        //calculate noteVal for x y pair:
        //0 0 = 0, 1 0 = 1, 0 1 = 16, 1 1 = 17
        noteVal = gridX + (gridY * gridWidth);

        //Make shortMessage
        ShortMessage shortMessage = new ShortMessage();
        try{
            shortMessage.setMessage(status, noteVal, noteVel);
        } catch(InvalidMidiDataException ex){
            ex.printStackTrace();
        }

        return shortMessage;
    }

    public static ShortMessage translateGridLedSetToMidi(OSCMessage oscMessage, Dimensions dimensions){
        List oscArgList = oscMessage.getArguments();
        int gridX = (int) oscArgList.get(0);
        int gridY = (int) oscArgList.get(1);
        int gridZ = (int) oscArgList.get(2);

        ShortMessage shortMessage = translateGridLedToMidi(gridX, gridY, gridZ, dimensions);

        return shortMessage;
    }

    public static List<ShortMessage> translateLedAllToMidi(OSCMessage oscMessage, Dimensions dimensions){
        //OSC stuff
        //s is either 1 or 0

        int s = (int) oscMessage.getArguments().get(0);
        //Build up Arraylist of Shortmessages - 1 is all on and 0 is all off
        ArrayList shortMessageList = new ArrayList();
        for(int i = 0; i < dimensions.getArea(); i++){
            try {
                ShortMessage shortMessage = new ShortMessage(144, i, s);
                shortMessageList.add(shortMessage);
            } catch(InvalidMidiDataException ex) {
                ex.printStackTrace();
            }
        }
        return shortMessageList;
    }

    public static List<ShortMessage> translateLedMapToMidi(OSCMessage oscMessage, Dimensions dimensions){
        //There will be 10 args xOffset, yOffset (determines which 8x8 quad is being lit)
        // then 8 additional args. Each additional arg is a bitmask of a row
        // (0-255, 0-255, 0-255, 0-255, 0-255, 0-255, 0-255, 0-255)
        //This is similar to LedRow and LedCol, but with 8 values instead of 1.
        List oscArgList = oscMessage.getArguments();
        int xOffset = (int) oscArgList.get(0);
        int yOffset = (int) oscArgList.get(1);

        //Build up Arraylist of Shortmessages from 8 bitmask arguments
        ArrayList shortMessageList = new ArrayList();
        //Iterate over the remaining args
        int counter2 = 0;
        for(int i = 2; i < oscArgList.size(); i++){
            //bitMask = a row of values 00000000 is all off 11111111 is all on, 00001111 is 1st 4 off 2nd 4 on
            int bitMask = (int) oscArgList.get(i);
            //binaryString does not have leading 0s, i.e. if bitmask = 10, binaryString = 1010, not 00001010
            String binaryString = Integer.toBinaryString(bitMask);
            //counter is for xOffset
            int counter = 0;
            //counter2 is for yOffset

            //Add leading 0s if fewer than 8 bits
            for(int j = 7; j >= binaryString.length(); j--){
                ShortMessage shortMessage = translateGridLedToMidi(xOffset + counter,
                                                                    yOffset + counter2,
                                                                    0,
                                                                    dimensions);
                shortMessageList.add(shortMessage);
                counter++;
            }
            byte[] bytes = binaryString.getBytes();
            //add data from message
            for(int j = 0; j < binaryString.length(); j++){

                byte b = bytes[j];
                int gridZ = 0;
                if ((b & 1) != 0){
                    gridZ = 1;
                }
                ShortMessage shortMessage = translateGridLedToMidi(xOffset + counter,
                        yOffset + counter2,
                        gridZ,
                        dimensions);

                shortMessageList.add(shortMessage);
                counter++;
            }
            counter2++;
        }

        return shortMessageList;
    }

    public static List<ShortMessage> translateLedRowToMidi(OSCMessage oscMessage, Dimensions dimensions){
        //OSC stuff
        //get args
        //This will always be 0 with a 64 grid - this represents the quad (group of 8x8)
        int xOffset = (int) oscMessage.getArguments().get(0);
        //This is the row you're starting on
        int gridY = (int) oscMessage.getArguments().get(1);

        int counter = 0;
        //Build up Arraylist of Shortmessages from binaryString
        ArrayList shortMessageList = new ArrayList();

        for(int i = 2; i < oscMessage.getArguments().size(); i++) {
            //ignore second argument for 8x8 grids - this is for leds 8 15
            if((dimensions.getArea()) == 64){
                if(counter > 0){
                    return shortMessageList;
                }
            }
            int bitmask = (int) oscMessage.getArguments().get(i);
            //There could potentially be a second bitmask for 8x16 grids
            //int bitmask2 = (int) oscMessage.getArguments().get(3);
            String binaryString = Integer.toBinaryString(bitmask);

            //Add leading 0s if fewer than 8 bits
            for (int j = 7; j >= binaryString.length(); j--) {
                ShortMessage shortMessage = translateGridLedToMidi(xOffset + counter,
                        gridY,
                        0,
                        dimensions);

                shortMessageList.add(shortMessage);

                counter++;
            }

            byte[] bytes = binaryString.getBytes();

            //add data from message
            for (int j = 0; j < binaryString.length(); j++) {
                //this is incorrect, but at least we're getting the right number of things now
                byte b = bytes[j];
                int gridZ = 0;
                if ((b & 1) != 0) {
                    gridZ = 1;
                }

                ShortMessage shortMessage = translateGridLedToMidi(xOffset + counter,
                        gridY,
                        gridZ,
                        dimensions);

                shortMessageList.add(shortMessage);
                counter++;
            }
        }
        //This isn't going to work - it needs to return a List<shortmessage>
        return shortMessageList;
    }

    public static List<ShortMessage> translateLedColToMidi(OSCMessage oscMessage, Dimensions dimensions){
        //OSC stuff
        //get args
        //This will always be 0 with a 64 grid - this represents the quad (group of 8x8)
        int gridX = (int) oscMessage.getArguments().get(0);
        //This is the row you're starting on
        int yOffset = (int) oscMessage.getArguments().get(1);

        int counter = 0;
        //Build up Arraylist of Shortmessages from binaryString
        ArrayList shortMessageList = new ArrayList();

        for(int i = 2; i < oscMessage.getArguments().size(); i++) {
            //ignore second argument for 8x8 grids - this is for leds 8 15
            if((dimensions.getArea()) == 64){
                if(counter > 0){
                    return shortMessageList;
                }
            }
            int bitmask = (int) oscMessage.getArguments().get(i);
            //There could potentially be a second bitmask for 8x16 grids
            //int bitmask2 = (int) oscMessage.getArguments().get(3);
            String binaryString = Integer.toBinaryString(bitmask);

            //Add leading 0s if fewer than 8 bits
            for (int j = 7; j >= binaryString.length(); j--) {
                ShortMessage shortMessage = translateGridLedToMidi(gridX,
                        yOffset + counter,
                        0,
                        dimensions);

                shortMessageList.add(shortMessage);

                counter++;
            }

            byte[] bytes = binaryString.getBytes();

            //add data from message
            for (int j = 0; j < binaryString.length(); j++) {
                //this is incorrect, but at least we're getting the right number of things now
                byte b = bytes[j];
                int gridZ = 0;
                if ((b & 1) != 0) {
                    gridZ = 1;
                }

                ShortMessage shortMessage = translateGridLedToMidi(gridX,
                        yOffset + counter,
                        gridZ,
                        dimensions);

                shortMessageList.add(shortMessage);
                counter++;
            }
        }
        //This isn't going to work - it needs to return a List<shortmessage>
        return shortMessageList;
    }

    //Level messages are for varibright grids. I'm not supporting this at this time, but need to handle these anyways.
    //translates Led Messages (varibright)

    public static ShortMessage translateGridLevelToMidi(int gridX, int gridY, int gridZ, Dimensions dimensions){
        //logic to get grid data to midi
        int status = 144; //noteOn message
        int noteVal;
        //calculate the width of the grid
        //TODO: Implement Velocity so it supports varibright
        int noteVel = 127;
        if(gridZ == 0){
            noteVel = 0;
        }


        if(dimensions.isInverted()){
            int maxValue = dimensions.getHeight() - 1;
            gridY = maxValue - gridY;
        }

        //calculate noteVal for x y pair:
        //0 0 = 0, 1 0 = 1, 0 1 = 16, 1 1 = 17
        noteVal = gridX + (gridY * dimensions.getWidth());


        //Make shortMessage
        ShortMessage shortMessage = new ShortMessage();
        try{
            shortMessage.setMessage(status, noteVal, noteVel);
        } catch(InvalidMidiDataException ex){
            ex.printStackTrace();
        }

        return shortMessage;
    }

    public static ShortMessage translateLedLevelSetToMidi(OSCMessage oscMessage, Dimensions dimensions){

        //Get data from Osc message
        int gridX = (int) oscMessage.getArguments().get(0);
        int gridY = (int) oscMessage.getArguments().get(1);
        int gridZ = (int) oscMessage.getArguments().get(2);

        int ledState = 1;
        //Translate ledLevel to ledSet (no varibright support)
        if(gridZ <= 4){
            ledState = 0;
        }

        ShortMessage shortMessage = translateGridLevelToMidi(gridX, gridY, ledState, dimensions);

        return shortMessage;
    }

    public static List<ShortMessage> translateLedLevelAllToMidi(OSCMessage oscMessage, Dimensions dimensions){
        //OSC stuff
        //s is a number between 0-255. Less than 5 is off, 5 or greater is on.
        int s = (int) oscMessage.getArguments().get(0);

        int ledState = 1;

        if(s <= 4){
            ledState = 0;
        }

        //Build up Arraylist of Shortmessages - 1 is all on and 0 is all off
        ArrayList shortMessageList = new ArrayList();
        for(int i = 0; i < dimensions.getArea(); i++){
            try {
                ShortMessage shortMessage = new ShortMessage(144, i, ledState);
                shortMessageList.add(shortMessage);
            } catch(InvalidMidiDataException ex) {
                ex.printStackTrace();
            }
        }
        return shortMessageList;
    }

    public static List<ShortMessage> translateLedLevelMapToMidi(OSCMessage oscMessage, Dimensions dimensions){

        //Build up Arraylist of Shortmessages from binaryString
        ArrayList shortMessageList = new ArrayList();

        //This will always be 0 with a 64 grid - this represents the quad (group of 8x8)
        int xOffset = (int) oscMessage.getArguments().get(0);
        //This is the row you're starting on
        int yOffset = (int) oscMessage.getArguments().get(1);

        //yOffset should never be greater than 0 on 8x8 and 8x16 grids
        if(yOffset > 0){
            return shortMessageList;
        }

        //xOffset should only be greater than 0 for 8x16 grids
        if((dimensions.getArea()) == 64) {
            if(xOffset > 0){
                return shortMessageList;
            }
        }

        List oscList = oscMessage.getArguments();

        //When do these increment?
        //Because of xOffset. when it's 0 this is fine but when it's 8 it's not.
        int counter = 0;
        for(int i = 2; i < oscList.size(); i++){
            int xCounter = counter % 8; // increments to 7 then resets
            int yCounter = (int) Math.floor(counter / 8); // 0-7 = 0, 8-15 = 1, ...

            int ledState = (int) oscList.get(i);
            if(ledState <= 4){
                ledState = 0;
            } else {
                ledState = 1;
            }

            int gridX = xOffset + xCounter;
            int gridY = yOffset + yCounter;

            ShortMessage shortMessage = translateGridLevelToMidi(gridX,
                                                                gridY,
                                                                ledState,
                                                                dimensions);
            shortMessageList.add(shortMessage);
            counter++;
        }

        return shortMessageList;
    }

    public static List<ShortMessage> translateLedLevelRowToMidi(OSCMessage oscMessage, Dimensions dimensions){

        ArrayList shortMessageList = new ArrayList<ShortMessage>();

        //oscArgList should have either 10 or 18 args xOffset, gridY, + number of buttons in col (8 or 16)
        List oscArgList = oscMessage.getArguments();

        //xOffset is either 0 or 8
        int xOffset = (int) oscArgList.get(0);

        int gridY = (int) oscArgList.get(1);

        if(dimensions.getArea() == 64) {
            if(xOffset > 0){   //xOffset should only be greater than 0 for 8x16 grids
                return shortMessageList;
            } else if (oscArgList.size() > 10) { //8x8 grids can only support messages with 10 args, some will have 18
                return shortMessageList;
            }
        }

        for(int i = 2; i < oscArgList.size(); i++){
            //Varibright is not supported - if intensity is 4 or less, it is 0, if greater than 4 it is 1
            int ledState = 1;
            if ((int) oscArgList.get(i) <= 4){
                ledState = 0;
            }
            ShortMessage shortMessage = translateGridLevelToMidi(xOffset + (i - 2),
                                                                gridY,
                                                                ledState,
                                                                dimensions);
            shortMessageList.add(shortMessage);
        }

        return shortMessageList;
    }

    public static List<ShortMessage> translateLedLevelColToMidi(OSCMessage oscMessage, Dimensions dimensions){

        ArrayList shortMessageList = new ArrayList<ShortMessage>();

        //oscArgList should have either 10 or 18 args xOffset, gridY, + number of buttons in col (8 or 16)
        List oscArgList = oscMessage.getArguments();

        int gridX = (int) oscArgList.get(0);
        //yOffset is either 0 or 8
        int yOffset = (int) oscArgList.get(1);

        if(dimensions.getArea() == 64) {
            if(yOffset > 0){   //yOffset should only be greater than 0 for 8x16 grids
                return shortMessageList;
            } else if (oscArgList.size() > 10) { //8x8 grids can only support messages with 10 args, some will have 18
                return shortMessageList;
            }
        }

        for(int i = 2; i < oscArgList.size(); i++){
            //Varibright is not supported - if intensity is 4 or less, it is 0, if greater than 4 it is 1
            int ledState = 1;
            if ((int) oscArgList.get(i) <= 4){
                ledState = 0;
            }
            ShortMessage shortMessage = translateGridLevelToMidi(gridX,
                                                                yOffset + (i - 2),
                                                                ledState,
                                                                dimensions);
            shortMessageList.add(shortMessage);
        }

        return shortMessageList;
    }
}
