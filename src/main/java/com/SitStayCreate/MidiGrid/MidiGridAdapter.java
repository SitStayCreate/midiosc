package com.SitStayCreate.MidiGrid;

import com.SitStayCreate.Constants;
import com.SitStayCreate.MidiGrid.LEDListeners.*;
import com.SitStayCreate.Serialosc.*;
import com.illposed.osc.*;
import com.illposed.osc.messageselector.JavaRegexAddressMessageSelector;

import javax.sound.midi.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;


public class MidiGridAdapter extends GridController implements Transmitter, Receiver {

    //Transmitter vars
    private Receiver receiver;

    //Represents the midi controller
    private HardwareDevice hardwareDevice;

    //Listeners
    private MGLEDSetListener mgledSetListener;
    private MGLEDAllListener mgledAllListener;
    private MGLEDMapListener mgledMapListener;
    private MGLEDRowListener mgledRowListener;
    private MGLEDColListener mgledColListener;
    private MGLEDLevelSetListener mgledLevelSetListener;
    private MGLEDLevelAllListener mgledLevelAllListener;
    private MGLEDLevelMapListener mgledLevelMapListener;
    private MGLEDLevelRowListener mgledLevelRowListener;
    private MGLEDLevelColListener mgledLevelColListener;


    public MidiGridAdapter(MonomeApp monomeApp,
                           Dimensions dimensions,
                           int portInNumber,
                           HardwareDevice hardwareDevice) throws IOException {
        super(monomeApp,
                new DecoratedOSCPortIn(portInNumber),
                new DecoratedOSCPortOut(monomeApp.getInetAddress(), monomeApp.getPortNumber()),
                dimensions);
        this.dimensions = dimensions;
        this.hardwareDevice = hardwareDevice;
        connectMidi();
        addLEDListeners();
    }

    @Override
    public void addLEDListeners(){
        int channel = hardwareDevice.getChannel();
        String ledSetSelectorRegex = Constants.LED_SET_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledSetMessageSelector = new JavaRegexAddressMessageSelector(ledSetSelectorRegex);
        mgledSetListener = new MGLEDSetListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledSetMessageSelector, mgledSetListener);

        String ledAllSelectorRegex = Constants.LED_ALL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledAllMessageSelector = new JavaRegexAddressMessageSelector(ledAllSelectorRegex);
        mgledAllListener = new MGLEDAllListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledAllMessageSelector, mgledAllListener);

        String ledMapSelectorRegex = Constants.LED_MAP_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledMapMessageSelector = new JavaRegexAddressMessageSelector(ledMapSelectorRegex);
        mgledMapListener = new MGLEDMapListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledMapMessageSelector, mgledMapListener);

        String ledRowSelectorRegex = Constants.LED_ROW_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledRowMessageSelector = new JavaRegexAddressMessageSelector(ledRowSelectorRegex);
        mgledRowListener = new MGLEDRowListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledRowMessageSelector, mgledRowListener);

        String ledColSelectorRegex = Constants.LED_COL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledColMessageSelector = new JavaRegexAddressMessageSelector(ledColSelectorRegex);
        mgledColListener = new MGLEDColListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledColMessageSelector, mgledColListener);

        String ledLevelSetSelectorRegex = Constants.LED_LEVEL_SET_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelSetMessageSelector = new JavaRegexAddressMessageSelector(ledLevelSetSelectorRegex);
        mgledLevelSetListener = new MGLEDLevelSetListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledLevelSetMessageSelector, mgledLevelSetListener);

        String ledLevelAllSelectorRegex = Constants.LED_LEVEL_ALL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelAllMessageSelector = new JavaRegexAddressMessageSelector(ledLevelAllSelectorRegex);
        mgledLevelAllListener = new MGLEDLevelAllListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledLevelAllMessageSelector, mgledLevelAllListener);

        String ledLevelMapSelectorRegex = Constants.LED_LEVEL_MAP_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelMapMessageSelector = new JavaRegexAddressMessageSelector(ledLevelMapSelectorRegex);
        mgledLevelMapListener = new MGLEDLevelMapListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledLevelMapMessageSelector, mgledLevelMapListener);

        String ledLevelRowSelectorRegex = Constants.LED_LEVEL_ROW_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelRowMessageSelector = new JavaRegexAddressMessageSelector(ledLevelRowSelectorRegex);
        mgledLevelRowListener = new MGLEDLevelRowListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledLevelRowMessageSelector, mgledLevelRowListener);

        String ledLevelColSelectorRegex = Constants.LED_LEVEL_COL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelColMessageSelector = new JavaRegexAddressMessageSelector(ledLevelColSelectorRegex);
        mgledLevelColListener = new MGLEDLevelColListener(dimensions, receiver, channel);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledLevelColMessageSelector, mgledLevelColListener);
    }


    public HardwareDevice getHardwareDevice() {
        return hardwareDevice;
    }

    public String getId() {
        return id;
    }

    @Override
    public DecoratedOSCPortIn getDecoratedOSCPortIn() {
        return decoratedOSCPortIn;
    }

    public DecoratedOSCPortOut getDecoratedOSCPortOut() {
        return decoratedOSCPortOut;
    }

    private void connectMidi() {
        //Get a transmitter from the physical hardware and connect it to this (receiver)
        hardwareDevice.getTransmitter().setReceiver(this);

        //Get a receiver from the physical hardware and connect it to this (transmitter)
        //It's worth noting that the receiver here is already a member in hardwareDevice
        //and it is being duplicated here. In order to fulfill the transmitter interface
        //it is necessary to have a method named setReceiver that takes a receiver as input.
        //Even though it is a duplicate reference, it is easier to understand how this
        //class fulfills the midi transmitter interface
        setReceiver(hardwareDevice.getReceiver());
    }

    //Receiver Methods
    @Override
    public void send(MidiMessage message, long timeStamp) {
        //Don't send messages to port 0, Max/MSP sends this as a place holder, but it leads to runtime exceptions
        if(decoratedOSCPortOut.getTargetPort() == 0){
            return;
        }
        try{
            OSCMessage oscMessage = OSCTranslator.translateMidiToOSC(message, dimensions, getPrefix(), hardwareDevice.getChannel());
            List oscArgs = oscMessage.getArguments();
            int z = (Integer) oscArgs.get(2);

            if(z < 0){
                return;
            }
            decoratedOSCPortOut.send(oscMessage);
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
    //This implements two abstract close methods - one in Transmitter and one in Receiver
    //It doesn't make sense to close the interfaces here, so the most sensible thing is
    //closing the open OSC ports.
    public void close() {
        try {
            decoratedOSCPortOut.close();
            decoratedOSCPortIn.close();
            hardwareDevice.close();
            //TODO: Rest of the stuff required to close this device
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Transmitter Methods
    @Override
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public Receiver getReceiver() {
        return receiver;
    }

    public void setChannel(int channel) {
        mgledSetListener.setChannel(channel);
        mgledAllListener.setChannel(channel);
        mgledRowListener.setChannel(channel);
        mgledColListener.setChannel(channel);
        mgledMapListener.setChannel(channel);
        mgledLevelSetListener.setChannel(channel);
        mgledLevelAllListener.setChannel(channel);
        mgledLevelRowListener.setChannel(channel);
        mgledLevelColListener.setChannel(channel);
        mgledLevelMapListener.setChannel(channel);
    }
}
