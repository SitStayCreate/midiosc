package com.SitStayCreate.MidiGrid;

import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class HardwareDevice {

    //Consider replacing MidiDevice transmittingMidiDevice with a transmitter
    private Transmitter transmitter;
    private Receiver receiver;
    private int channel;

    public HardwareDevice(){
    }

    public HardwareDevice(Receiver receiver, Transmitter transmitter, int channel){
        this.receiver = receiver;
        this.transmitter = transmitter;
        this.channel = channel;
    }

    public Transmitter getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(Transmitter transmitter) {
        this.transmitter = transmitter;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void close(){
        transmitter.close();
        receiver.close();
    }
}
