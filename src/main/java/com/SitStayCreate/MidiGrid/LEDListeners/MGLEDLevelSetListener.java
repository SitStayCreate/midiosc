package com.SitStayCreate.MidiGrid.LEDListeners;

import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelSetListener;
import com.SitStayCreate.MidiGrid.OSCTranslator;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.List;

public class MGLEDLevelSetListener extends LEDLevelSetListener {

    Dimensions dims;
    Receiver receiver;
    private int channel;

    public MGLEDLevelSetListener(Dimensions dims, Receiver receiver, int channel){
        this.dims = dims;
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override
    public void setLEDLevelState(int x, int y, int z) {

        //y is always less than 8 on grids with 8 rows
        if(dims.getHeight() == 8){
            if(y >= 8){
                return;
            }
        }

        //x is always less than 8 on grids with 8 cols
        if(dims.getWidth() == 8){
            if(x >= 8){
                return;
            }
        }

        ShortMessage shortMessage = OSCTranslator.translateGridLevelToMidi(x, y, z, dims, channel);

        receiver.send(shortMessage, -1);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
