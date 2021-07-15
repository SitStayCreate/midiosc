package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDRowListener;
import com.SitStayCreate.MidiGrid.OSCTranslator;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public class MGLEDRowListener extends LEDRowListener {
    Dimensions dims;
    Receiver receiver;
    private int channel;


    public MGLEDRowListener(Dimensions dims, Receiver receiver, int channel){
        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override
    public void setLEDRowState(String binaryString, int xOffset, int xCounter, int y) {

        if(dims.getWidth() == 8) {
            if(xOffset > 0){   //xOffset should only be greater than 0 for 8x16 grids
                return;
            } else if (xCounter >= 8) { //8x8 grids can only support messages with 10 args, some will have 18
                return;
            }
        }

        //y should not be greater than 7 on grids with 8 rows
        if(dims.getHeight() == 8){
            if(y>=8){
                return;
            }
        }

        //Add leading 0s if fewer than 8 bits
        for (int j = 7; j >= binaryString.length(); j--) {
            ShortMessage shortMessage = OSCTranslator.translateGridLedToMidi(xOffset + xCounter,
                    y,
                    0,
                    dims,
                    channel);
            receiver.send(shortMessage, -1);
            xOffset++;
        }

        byte[] bytes = binaryString.getBytes();

        //add data from message
        for (int j = 0; j < binaryString.length(); j++) {
            byte b = bytes[j];
            int gridZ = 0;
            if ((b & 1) != 0) {
                gridZ = 1;
            }

            ShortMessage shortMessage = OSCTranslator.translateGridLedToMidi(xOffset + xCounter,
                    y,
                    gridZ,
                    dims,
                    channel);
            receiver.send(shortMessage, -1);

            xCounter++;
        }
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
