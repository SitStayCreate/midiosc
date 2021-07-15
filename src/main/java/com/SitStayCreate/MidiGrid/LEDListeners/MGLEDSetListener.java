package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.MidiGrid.OSCTranslator;
import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDSetListener;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MGLEDSetListener extends LEDSetListener {
    Dimensions dims;
    Receiver receiver;
    private int channel;

    public MGLEDSetListener(Dimensions dims, Receiver receiver, int channel){
        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override
    public void setLEDState(int x, int y, int z) {

        //prevents index out of bounds errors when the grid height is < 16
        if(dims.getHeight() == 8){
            if(y >= 8){
                return;
            }
        }

        //prevents index out of bounds errors when the grid width is < 16
        if(dims.getWidth() == 8){
            if(x >= 8){
                return;
            }
        }

        ShortMessage shortMessage = OSCTranslator.translateGridLedToMidi(x, y, z, dims, channel);
        receiver.send(shortMessage, -1);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
