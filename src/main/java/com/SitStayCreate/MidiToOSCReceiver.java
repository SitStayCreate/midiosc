package com.SitStayCreate;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCSerializeException;

import javax.sound.midi.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MidiToOSCReceiver implements Receiver {

    private OSCPortOutWrapper oscPortOutWrapper;
    private Dimensions dimensions;
    private String prefix;

    public MidiToOSCReceiver(OSCPortOutWrapper oscPortOutWrapper, Dimensions dimensions, String prefix){
        this.oscPortOutWrapper = oscPortOutWrapper;
        this.dimensions = dimensions;
        this.prefix = prefix;
    }

    public OSCPortOutWrapper getOscPortOutWrapper() {
        return oscPortOutWrapper;
    }

    public void setOscPortOutWrapper(OSCPortOutWrapper oscPortOutWrapper) {
        this.oscPortOutWrapper = oscPortOutWrapper;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    //Midi methods for receiver interface
    @Override
    public void send(MidiMessage message, long timeStamp) {
        //Don't send messages to port 0, Max/MSP sends this as a place holder, but it leads to runtime exceptions
        if(oscPortOutWrapper.getPortOutNumber() == 0){
            return;
        }
        try{
            OSCMessage oscMessage = OSCTranslator.translateMidiToOSC(message, dimensions, prefix);
            oscPortOutWrapper.getOscPortOut().send(oscMessage);
        } catch (SocketException ex){
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        } catch (OSCSerializeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        //This is supposed to close MidiReceiver resources, but this is the most sensible thing to do here
        try {
            oscPortOutWrapper.getOscPortOut().disconnect();
            oscPortOutWrapper.getOscPortOut().close();
        } catch (IOException ioex){
            ioex.printStackTrace();
        }

    }
}
