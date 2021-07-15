package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDAllListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

//TODO: support 16 channels
public class MGLEDAllListener extends LEDAllListener {

    Dimensions dims;
    Receiver receiver;
    private int channel;

    public MGLEDAllListener(Dimensions dims, Receiver receiver, int channel){

        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override
    public void setLedAllState(int s) {
        int status = 144;
        //Sets all LEDs off or all LEDs on
        for(int i = 0; i < dims.getArea(); i++){
            try {
                ShortMessage shortMessage = new ShortMessage(channel + status, i, s);
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
