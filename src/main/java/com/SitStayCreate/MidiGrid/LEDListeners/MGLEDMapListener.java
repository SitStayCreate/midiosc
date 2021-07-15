package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDMapListener;
import com.SitStayCreate.MidiGrid.OSCTranslator;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MGLEDMapListener extends LEDMapListener {

    Dimensions dims;
    Receiver receiver;
    private int channel;

    public MGLEDMapListener(Dimensions dims, Receiver receiver, int channel){
        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override
    public void setLEDMap(String binaryString, int xOffset, int yOffset, int yCounter) {
        //counter is for xOffset
        int xCounter = 0;

        //xOffset should only be greater than 0 for *x16 grids
        if((dims.getWidth()) == 8) {
            if(xOffset > 0){
                return;
            }
        }

        //yOffset should only be greater than 0 for 16x* grids
        if((dims.getHeight()) == 8) {
            if(yOffset > 0){
                return;
            }
        }

        //Add leading 0s if fewer than 8 bits
        for(int j = 7; j >= binaryString.length(); j--){

            ShortMessage shortMessage = OSCTranslator.translateGridLedToMidi(xOffset + xCounter,
                    yOffset + yCounter,
                    0,
                    dims,
                    channel);

            receiver.send(shortMessage, -1);

            xCounter++;
        }
        byte[] bytes = binaryString.getBytes();
        //add data from message
        for(int j = 0; j < binaryString.length(); j++){

            byte b = bytes[j];
            int gridZ = 0;
            if ((b & 1) != 0){
                gridZ = 1;
            }

            ShortMessage shortMessage = OSCTranslator.translateGridLedToMidi(xOffset + xCounter,
                    yOffset + yCounter,
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
