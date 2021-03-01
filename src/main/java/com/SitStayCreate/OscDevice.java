package com.SitStayCreate;

import com.illposed.osc.*;
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector;
import com.illposed.osc.transport.udp.OSCPortIn;
import com.illposed.osc.transport.udp.OSCPortOut;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class OscDevice {
    //MIDI in -> OSCDevice out
    private MidiToOSCReceiver midiToOSCReceiver;
    //MIDI out <- OSCDevice in
    private OscToMidiTransmitter oscToMidiTransmitter;

    //Represents the midi controller
    private HardwareDevice hardwareDevice;

    //Ports
    private OSCPortOutWrapper oscPortOutWrapper;
    private OSCPortInWrapper oscPortInWrapper;
    private App destinationApp;
    private String id, prefix;
    private Dimensions dimensions;

    public OscDevice(Dimensions dimensions, int portInNumber, String id){
        try{
            this.dimensions = dimensions;
            destinationApp = new App(InetAddress.getByName("localhost"), 8000);
            //default prefix is /monome
            prefix = "/monome";
            this.id = id;


            //Create IO ports
            OSCPortOut oscPortOut = new OSCPortOut(destinationApp.getInetAddress(), destinationApp.getPortNumber());
            OSCPortIn oscPortIn = new OSCPortIn(portInNumber);
            //Wrap them in a wrapper class so we can get the port numbers conveniently
            oscPortOutWrapper = new OSCPortOutWrapper(oscPortOut, destinationApp.getPortNumber());
            oscPortInWrapper = new OSCPortInWrapper(oscPortIn, portInNumber);

            midiToOSCReceiver = new MidiToOSCReceiver(oscPortOutWrapper, this.dimensions, prefix);
            oscToMidiTransmitter = new OscToMidiTransmitter(oscPortInWrapper, this.dimensions);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        String sysPrefixString = "/sys/prefix";
        MessageSelector sysPrefixSelector = new OSCPatternAddressMessageSelector(sysPrefixString);
        //Create an event listener
        SysPrefixMessageListener sysPrefixMessageListener = new SysPrefixMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(sysPrefixSelector, sysPrefixMessageListener);

        String sysPortString = "/sys/port";
        MessageSelector sysPortSelector = new OSCPatternAddressMessageSelector(sysPortString);
        //Create an event listener
        SysPortMessageListener sysPortMessageListener = new SysPortMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(sysPortSelector, sysPortMessageListener);

        String sysInfoString = "/sys/info";
        MessageSelector sysInfoSelector = new OSCPatternAddressMessageSelector(sysInfoString);
        //Create an event listener
        SysInfoMessageListener sysInfoMessageListener = new SysInfoMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(sysInfoSelector, sysInfoMessageListener);
    }

    public OscDevice(MidiToOSCReceiver midiToOSCReceiver, OscToMidiTransmitter oscToMidiTransmitter){
        this.midiToOSCReceiver = midiToOSCReceiver;
        this.oscToMidiTransmitter = oscToMidiTransmitter;
    }

    public App getDestinationApp() {
        return destinationApp;
    }

    public HardwareDevice getHardwareDevice() {
        return hardwareDevice;
    }

    public String getId() {
        return id;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    //Midi Device -> External App
    public MidiToOSCReceiver getMidiToOSCReceiver() {
        return midiToOSCReceiver;
    }

    //External App -> Midi Device
    public OscToMidiTransmitter getOscToMidiTransmitter() {
        return oscToMidiTransmitter;
    }

    public OSCPortInWrapper getOscPortInWrapper() {
        return oscPortInWrapper;
    }

    public OSCPortOutWrapper getOscPortOutWrapper() {
        return oscPortOutWrapper;
    }

    public void setMidiToOSCReceiver(MidiToOSCReceiver midiToOSCReceiver) {
        this.midiToOSCReceiver = midiToOSCReceiver;
    }

    public void setOscToMidiTransmitter(OscToMidiTransmitter oscToMidiTransmitter) {
        this.oscToMidiTransmitter = oscToMidiTransmitter;
    }

    public void setHardwareDevice(HardwareDevice hardwareDevice) {
        try{
            this.hardwareDevice = hardwareDevice;
            //Midi in -> Osc out
            Transmitter transmitter = hardwareDevice.getTransmittingMidiDevice().getTransmitter();
            transmitter.setReceiver(getMidiToOSCReceiver());

            //Midi out <- Osc in
            Receiver receiver = hardwareDevice.getReceivingMidiDevice().getReceiver();
            getOscToMidiTransmitter().setReceiver(receiver);
        } catch(MidiUnavailableException ex){
            ex.printStackTrace();
        }

    }

    private class SysPrefixMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            String prefix = (String) event.getMessage().getArguments().get(0);
            midiToOSCReceiver.setPrefix(prefix);
        }
    }

    private class SysPortMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            int portOutNumber = (int) event.getMessage().getArguments().get(0);
            try {
                InetAddress inetAddress = InetAddress.getByName("localhost");
                oscPortOutWrapper.setOscPortOut(new OSCPortOut(inetAddress, portOutNumber));
                oscPortOutWrapper.setPortOutNumber(portOutNumber);
                midiToOSCReceiver.setOscPortOutWrapper(oscPortOutWrapper);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class SysInfoMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {

            List oscArgs = event.getMessage().getArguments();
            // /sys/info si <host> <port>
            if(oscArgs.size() == 2){
                sendInfo((String) oscArgs.get(0), (int) oscArgs.get(1));
                // /sys/info i <port>
            } else if(oscArgs.size() == 1){
                sendInfo("localhost", (int) oscArgs.get(0));
                // /sys/info
            } else {
                sendInfo(destinationApp.getInetAddress().getHostAddress(), destinationApp.getPortNumber());
            }
        }

        // /sys/info si
        private void sendInfo(String host, int port){
            List<OSCMessage> oscMessages = new ArrayList<>();

            //create OSCMessages
            List sysPortArgs = new ArrayList();
            sysPortArgs.add(destinationApp.getPortNumber());
            OSCMessage sysPort = new OSCMessage("/sys/port", sysPortArgs, new OSCMessageInfo("i"));
            oscMessages.add(sysPort);

            List sysHostArgs = new ArrayList();
            sysHostArgs.add(destinationApp.getInetAddress().getHostAddress());
            OSCMessage sysHost = new OSCMessage("/sys/host", sysHostArgs, new OSCMessageInfo("s"));
            oscMessages.add(sysHost);

            List sysIdArgs = new ArrayList();
            sysIdArgs.add(id);
            OSCMessage sysId = new OSCMessage("/sys/id", sysIdArgs, new OSCMessageInfo("s"));
            oscMessages.add(sysId);

            List sysPrefixArgs = new ArrayList();
            sysPrefixArgs.add(prefix);
            OSCMessage sysPrefix = new OSCMessage("/sys/prefix", sysPrefixArgs, new OSCMessageInfo("s"));
            oscMessages.add(sysPrefix);

            List sysRotationArgs = new ArrayList();
            sysRotationArgs.add(0);
            OSCMessage sysRotation = new OSCMessage("/sys/rotation", sysRotationArgs, new OSCMessageInfo("i"));
            oscMessages.add(sysRotation);

            List sysSizeArgs = new ArrayList();
            sysSizeArgs.add(dimensions.getHeight());
            sysSizeArgs.add(dimensions.getWidth());
            OSCMessage sysSize = new OSCMessage("/sys/size", sysSizeArgs, new OSCMessageInfo("ii"));
            oscMessages.add(sysSize);

            try{
                //create a temp port out to send a response
                InetAddress inetAddress = InetAddress.getByName(host);
                OSCPortOut tempOscPortOut = new OSCPortOut(inetAddress, port);
                for(OSCMessage oscMessage : oscMessages){
                    tempOscPortOut.send(oscMessage);
                }
                tempOscPortOut.disconnect();
                tempOscPortOut.close();
            } catch(IOException ex){
                ex.printStackTrace();
            } catch (OSCSerializeException ex){
                ex.printStackTrace();
            }
        }
    }
}
