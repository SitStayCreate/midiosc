package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelMapListener;
import com.SitStayCreate.MidiGrid.OSCTranslator;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.List;

public class MGLEDLevelMapListener extends LEDLevelMapListener {

    Dimensions dims;
    Receiver receiver;
    private int channel;

    public MGLEDLevelMapListener(Dimensions dims, Receiver receiver, int channel){
        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override
    public void setLEDLevelMap(int xOffset, int yOffset, int ledState, int counter) {
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

        int xCounter = counter % 8; // increments to 7 then resets
        int yCounter = (int) Math.floor(counter / 8); // 0-7 = 0, 8-15 = 1, ...

        int x = xOffset + xCounter;
        int y = yOffset + yCounter;

        ShortMessage shortMessage = OSCTranslator.translateGridLevelToMidi(x,
                y,
                ledState,
                dims, channel);
        receiver.send(shortMessage, -1);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
