package com.SitStayCreate.MidiGrid;

import com.SitStayCreate.Constants;
import com.SitStayCreate.Serialosc.Dimensions;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCMessageInfo;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

//TODO: Test this with mpd226
public class OSCTranslator {

    private static OSCMessage makeOSCList(String prefix, int x, int y, int z){
        String addressString = String.format(Constants.FORMAT_STRING, prefix);
        //make this a list
        List<Integer> oscArgs = new ArrayList<>();
        oscArgs.add(x);
        oscArgs.add(y);
        oscArgs.add(z);

        OSCMessageInfo oscMessageInfo = new OSCMessageInfo(Constants.FORMAT_TYPE_TAG);
        OSCMessage oscMessage = new OSCMessage(addressString, oscArgs, oscMessageInfo);

        return oscMessage;
    }

    //MIDI Device->External MonomeApp
    public static OSCMessage translateMidiToOSC(MidiMessage midiMessage, Dimensions dimensions, String prefix, int channel) {
        int status = midiMessage.getStatus();

        byte[] midiMessageByteArray = midiMessage.getMessage();
        int midiData1 = midiMessageByteArray[1];
        int midiData2 = midiMessageByteArray[2];
        int width = dimensions.getWidth();
        //convert midi to matrix
        int gridX = midiData1 % width;
        int gridY = (int) Math.floor(midiData1 / width);

        System.out.println("status: " + status);
        //Note off
        if(status >= 128 && status < 144){
            channel += 128;
            if(status == channel){
                System.out.println("Channel: " + channel);
                //invert after osc conversion
                if(dimensions.isInverted()){
                    int maxValue = dimensions.getHeight() - 1;
                    gridY = maxValue - gridY;
                }
                return makeOSCList(prefix, gridX, gridY, 0);
            }
        }

        //Note on messages and Note off messages
        if(status >= 144 && status < 160){
            channel += 144;

            //NoteOn message
            if(status == channel) {
                System.out.println("Channel: " + channel);
                int gridStatus = 0;
                if (midiData2 > 0) {
                    gridStatus = 1;
                }

                //invert after osc conversion
                if(dimensions.isInverted()){
                    int maxValue = dimensions.getHeight() - 1;
                    gridY = maxValue - gridY;
                }
                return makeOSCList(prefix, gridX, gridY, gridStatus);
            }
        }

        //this message will be ignored
        return makeOSCList(prefix, -1, -1, -1);
    }

    //External MonomeApp->MIDI Device
    //translates Led Messages (monobright)
    public static ShortMessage translateGridLedToMidi(int gridX, int gridY, int gridZ, Dimensions dimensions, int channel){
        int status = 144;
        //logic to get grid data to midi
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

        ShortMessage shortMessage = new ShortMessage();
        try {
            shortMessage.setMessage(status + channel, noteVal, noteVel);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        return shortMessage;
    }

    //translates Led Messages (varibright)
    public static ShortMessage translateGridLevelToMidi(int gridX, int gridY, int ledState, Dimensions dimensions, int channel){
        int status = 144;
        //logic to get grid data to midi
        int noteVal;
        //calculate the width of the grid
        int noteVel;
        if(ledState < 4){
            noteVel = 0;
        } else if(ledState < 8){
            noteVel = 80;
        } else if(ledState < 12){
            noteVel = 105;
        } else {
            noteVel = 127;
        }

        if(dimensions.isInverted()){
            int maxValue = dimensions.getHeight() - 1;
            gridY = maxValue - gridY;
        }

        //calculate noteVal for x y pair:
        //0 0 = 0, 1 0 = 1, 0 1 = 16, 1 1 = 17
        noteVal = gridX + (gridY * dimensions.getWidth());

        ShortMessage shortMessage = new ShortMessage();
        try {
            shortMessage.setMessage(status + channel, noteVal, noteVel);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        return shortMessage;
    }

}
