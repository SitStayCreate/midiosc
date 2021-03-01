package com.SitStayCreate;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;
import com.illposed.osc.messageselector.JavaRegexAddressMessageSelector;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import java.util.List;

public class OscToMidiTransmitter implements Transmitter{

    private OSCPortInWrapper oscPortInWrapper;
    private Receiver receiver;
    private Dimensions dimensions;

    //This should always have a port number assigned when it is created
    public OscToMidiTransmitter(OSCPortInWrapper oscPortInWrapper, Dimensions dimensions){

        this.oscPortInWrapper = oscPortInWrapper;
        this.dimensions = dimensions;

        //Create listeners for open port
        addOscMessageListeners();
        oscPortInWrapper.getOscPortIn().startListening();
    }


    public OSCPortInWrapper getOscPortInWrapper() {
        return oscPortInWrapper;
    }

    public void setOscPortInWrapper(OSCPortInWrapper oscPortInWrapper) {
        this.oscPortInWrapper = oscPortInWrapper;
    }

    public void addOscMessageListeners(){

        String ledSetSelectorRegex = "/.*/grid/led/set";
        JavaRegexAddressMessageSelector ledSetMessageSelector = new JavaRegexAddressMessageSelector(ledSetSelectorRegex);
        LEDSetMessageListener ledSetMessageListener = new LEDSetMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledSetMessageSelector, ledSetMessageListener);

        String ledAllSelectorRegex = String.format("/.*/grid/led/all");
        JavaRegexAddressMessageSelector ledAllMessageSelector = new JavaRegexAddressMessageSelector(ledAllSelectorRegex);
        LEDAllMessageListener ledAllMessageListener = new LEDAllMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledAllMessageSelector, ledAllMessageListener);

        String ledMapSelectorRegex = String.format("/.*/grid/led/map");
        JavaRegexAddressMessageSelector ledMapMessageSelector = new JavaRegexAddressMessageSelector(ledMapSelectorRegex);
        LEDMapMessageListener ledMapMessageListener = new LEDMapMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledMapMessageSelector, ledMapMessageListener);

        String ledRowSelectorRegex = String.format("/.*/grid/led/row");
        JavaRegexAddressMessageSelector ledRowMessageSelector = new JavaRegexAddressMessageSelector(ledRowSelectorRegex);
        LEDRowMessageListener ledRowMessageListener = new LEDRowMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledRowMessageSelector, ledRowMessageListener);

        String ledColSelectorRegex = String.format("/.*/grid/led/col");
        JavaRegexAddressMessageSelector ledColMessageSelector = new JavaRegexAddressMessageSelector(ledColSelectorRegex);
        LEDColMessageListener ledColMessageListener = new LEDColMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledColMessageSelector, ledColMessageListener);

        String ledLevelSetSelectorRegex = String.format("/.*/grid/led/level/set");
        JavaRegexAddressMessageSelector ledLevelSetMessageSelector = new JavaRegexAddressMessageSelector(ledLevelSetSelectorRegex);
        LEDLevelSetMessageListener ledLevelSetMessageListener = new LEDLevelSetMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledLevelSetMessageSelector, ledLevelSetMessageListener);

        String ledLevelAllSelectorRegex = String.format("/.*/grid/led/level/all");
        JavaRegexAddressMessageSelector ledLevelAllMessageSelector = new JavaRegexAddressMessageSelector(ledLevelAllSelectorRegex);
        LEDLevelAllMessageListener ledLevelAllMessageListener = new LEDLevelAllMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledLevelAllMessageSelector, ledLevelAllMessageListener);

        String ledLevelMapSelectorRegex = String.format("/.*/grid/led/level/map");
        JavaRegexAddressMessageSelector ledLevelMapMessageSelector = new JavaRegexAddressMessageSelector(ledLevelMapSelectorRegex);
        LEDLevelMapMessageListener ledLevelMapMessageListener = new LEDLevelMapMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledLevelMapMessageSelector, ledLevelMapMessageListener);

        String ledLevelRowSelectorRegex = String.format("/.*/grid/led/level/row");
        JavaRegexAddressMessageSelector ledLevelRowMessageSelector = new JavaRegexAddressMessageSelector(ledLevelRowSelectorRegex);
        LEDLevelRowMessageListener ledLevelRowMessageListener = new LEDLevelRowMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledLevelRowMessageSelector, ledLevelRowMessageListener);

        String ledLevelColSelectorRegex = String.format("/.*/grid/led/level/col");
        JavaRegexAddressMessageSelector ledLevelColMessageSelector = new JavaRegexAddressMessageSelector(ledLevelColSelectorRegex);
        LEDLevelColMessageListener ledLevelColMessageListener = new LEDLevelColMessageListener();
        oscPortInWrapper.getOscPortIn().getDispatcher().addListener(ledLevelColMessageSelector, ledLevelColMessageListener);
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

    @Override
    public void close() {

    }


    //monobright grids
    private class LEDSetMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            ShortMessage shortMessage = OSCTranslator.translateGridLedSetToMidi(event.getMessage(),
                                                                                dimensions);
            receiver.send(shortMessage, -1);
        }
    }

    private class LEDAllMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedAllToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }

    private class LEDMapMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedMapToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }

    private class LEDRowMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedRowToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }

    private class LEDColMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedColToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }

    //varibright grids
    private class LEDLevelSetMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            ShortMessage shortMessage = OSCTranslator.translateLedLevelSetToMidi(event.getMessage(), dimensions);
            receiver.send(shortMessage, -1);
        }
    }

    private class LEDLevelAllMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedLevelAllToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }

    private class LEDLevelMapMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedLevelMapToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }

    private class LEDLevelRowMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedLevelRowToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }

    private class LEDLevelColMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List<ShortMessage> shortMessageList = OSCTranslator.translateLedLevelColToMidi(event.getMessage(), dimensions);
            for(ShortMessage shortMessage : shortMessageList){
                receiver.send(shortMessage, -1);
            }
        }
    }
}