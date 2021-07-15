package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelRowListener;
import com.SitStayCreate.MidiGrid.OSCTranslator;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.List;

public class MGLEDLevelRowListener extends LEDLevelRowListener {
    Dimensions dims;
    Receiver receiver;
    private int channel;

    public MGLEDLevelRowListener(Dimensions dims, Receiver receiver, int channel){
        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }


    @Override
    public void setLEDLevelRow(int xOffset, int gridY, int xCounter, int ledState) {
        if(dims.getWidth() == 8) {
            //xOffset should only be greater than 0 for 8x16 grids
            if(xOffset > 0){
                return;
            //8x8 grids can only support messages with 10 args, some will have 18
            } else if (xCounter >= 8) {
                return;
            }
        }

        //y cannot be more than 7 on grids with 8 rows
        if(dims.getHeight() == 8){
            if(gridY >= 8){
                return;
            }
        }

        ShortMessage shortMessage = OSCTranslator.translateGridLevelToMidi(xOffset + xCounter,
                gridY,
                ledState,
                dims,
                channel);
        receiver.send(shortMessage, -1);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

}
