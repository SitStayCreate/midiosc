package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelAllListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

//TODO: support 16 channels
public class MGLEDLevelAllListener extends LEDLevelAllListener {

    Dimensions dims;
    Receiver receiver;
    private int channel;

    public MGLEDLevelAllListener(Dimensions dims, Receiver receiver, int channel){
        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override
    public void setLEDLevelAll(int s) {
        int status = 144;
        //s is a number between 0-255. Less than 5 is off, 5 or greater is on.

        for(int i = 0; i < dims.getArea(); i++){
            try {
                ShortMessage shortMessage = new ShortMessage(status + channel, i, s);
                receiver.send(shortMessage, -1);
            } catch(InvalidMidiDataException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
