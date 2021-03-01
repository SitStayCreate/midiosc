package com.SitStayCreate;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class HardwareDevice {

    private MidiDevice receivingMidiDevice, transmittingMidiDevice;

    public HardwareDevice(){
    }

    public HardwareDevice(MidiDevice receivingMidiDevice, MidiDevice transmittingMidiDevice){
        this.receivingMidiDevice = receivingMidiDevice;
        this.transmittingMidiDevice = transmittingMidiDevice;
    }

    public void setReceivingMidiDevice(MidiDevice receivingMidiDevice) {
        this.receivingMidiDevice = receivingMidiDevice;
    }

    public void setTransmittingMidiDevice(MidiDevice transmittingMidiDevice) {
        this.transmittingMidiDevice = transmittingMidiDevice;
    }


    public MidiDevice getReceivingMidiDevice() {
        return receivingMidiDevice;
    }

    public MidiDevice getTransmittingMidiDevice() {
        return transmittingMidiDevice;
    }
}
