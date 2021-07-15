package com.SitStayCreate.GUI;

import com.SitStayCreate.MidiGrid.HardwareDevice;
import com.SitStayCreate.Serialosc.Dimensions;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Main purpose is creating the hardware device
public class MidiPanel extends JPanel {

    private JComboBox midiInComboBox, midiOutComboBox, chComboBox;
    private Map<String, MidiDevice> midiOutDeviceMap, midiInDeviceMap;
    private List<MidiDevice.Info> midiOutDevicesInfo, midiInDevicesInfo;
    private JRadioButton dimsRB1, dimsRB2;
    private JCheckBox invertedCheckBox;

    public MidiPanel(int font) {
        //Setup the GUI stuff
        setMidiInMap();
        setMidiOutMap();

        int bigFont = font;
        int smallFont = font - 2;
        Font labelFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font cboxFont = new Font(Font.MONOSPACED, Font.PLAIN, smallFont);
        Font rbuttonFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font chkboxFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);

        //MIDI In
        setBackground(Color.DARK_GRAY);
        GridBagLayout bagLayout = new GridBagLayout();
        setLayout(bagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 5, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel midiInLabel = new JLabel("Midi In: ");
        midiInLabel.setForeground(Color.WHITE);
        midiInLabel.setFont(labelFont);
        constraints.gridx = 0;
        constraints.gridy = 0;
        bagLayout.setConstraints(midiInLabel, constraints);
        add(midiInLabel);

        midiInComboBox = new JComboBox<>();
        midiInComboBox.setFont(cboxFont);
        for (String midiOutDevice : displayMidiInInfo()) {
            midiInComboBox.addItem(midiOutDevice);
        }
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        bagLayout.setConstraints(midiInComboBox, constraints);
        add(midiInComboBox);

        //MIDI Out
        JLabel midiOutLabel = new JLabel("Midi Out: ");
        midiOutLabel.setForeground(Color.WHITE);
        midiOutLabel.setFont(labelFont);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        bagLayout.setConstraints(midiOutLabel, constraints);
        add(midiOutLabel);


        midiOutComboBox = new JComboBox<>();
        midiOutComboBox.setFont(cboxFont);
        for(String midiOutDevice : displayMidiOutInfo()){
            midiOutComboBox.addItem(midiOutDevice);
        }
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        bagLayout.setConstraints(midiOutComboBox, constraints);
        add(midiOutComboBox);

        //Dimensions

        JLabel dimsLabel = new JLabel("Dimens: ");
        dimsLabel.setFont(labelFont);
        dimsLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(0, 0, 5, 0);
        bagLayout.setConstraints(dimsLabel, constraints);
        add(dimsLabel);


        ButtonGroup sizeRadioButtons = new ButtonGroup();

        dimsRB1 = new JRadioButton("64");
        dimsRB1.setFont(rbuttonFont);
        dimsRB1.setForeground(Color.WHITE);
        dimsRB1.setBackground(Color.DARK_GRAY);
        dimsRB1.setSelected(true);
        constraints.gridx = 1;
        constraints.gridy = 2;
        bagLayout.setConstraints(dimsRB1, constraints);
        sizeRadioButtons.add(dimsRB1);
        add(dimsRB1);

        dimsRB2 = new JRadioButton("128h");
        dimsRB2.setFont(rbuttonFont);
        dimsRB2.setForeground(Color.WHITE);
        dimsRB2.setBackground(Color.DARK_GRAY);
        constraints.gridx = 2;
        constraints.gridy = 2;
        bagLayout.setConstraints(dimsRB2, constraints);
        sizeRadioButtons.add(dimsRB2);
        add(dimsRB2);

        invertedCheckBox = new JCheckBox("Invert Y-Axis");
        invertedCheckBox.setFont(chkboxFont);
        invertedCheckBox.setForeground(Color.WHITE);
        invertedCheckBox.setBackground(Color.DARK_GRAY);
        constraints.gridx = 2;
        constraints.gridy = 3;
        bagLayout.setConstraints(invertedCheckBox, constraints);
        add(invertedCheckBox);

        //Channel select
        JLabel chLabel = new JLabel("Channel: ");
        chLabel.setForeground(Color.WHITE);
        chLabel.setFont(labelFont);
        constraints.gridx = 0;
        constraints.gridy = 3;
        bagLayout.setConstraints(chLabel, constraints);
        add(chLabel);

        chComboBox = new JComboBox<>();
        chComboBox.setFont(cboxFont);
        for(int i = 0; i < 16; i++){
            chComboBox.addItem(String.valueOf(i + 1));
        }
        constraints.gridx = 1;
        constraints.gridy = 3;
        bagLayout.setConstraints(chComboBox, constraints);
        add(chComboBox);

    }

    private void setMidiOutMap(){
        Map<String, MidiDevice> midiDeviceMap = new HashMap<>();
        List<MidiDevice.Info> midiOutInfoList = new ArrayList<>();

        //Midi info
        MidiDevice.Info[] midiInfoArr = MidiSystem.getMidiDeviceInfo();

        if (midiInfoArr.length > 0) {
            for (MidiDevice.Info midiInfo : midiInfoArr) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(midiInfo);

                    if (device instanceof Synthesizer) {
                        //do nothing
                    } else if(device instanceof Sequencer) {
                        //do nothing
                    } else if (device.getMaxReceivers() != 0) {
                        midiDeviceMap.put(midiInfo.getName(), device);
                        midiOutInfoList.add(midiInfo);
                    }
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }

        this.midiOutDeviceMap = midiDeviceMap;
        this.midiOutDevicesInfo = midiOutInfoList;
    }

    private void setMidiInMap(){
        //TODO: Make these a map or something
        Map<String, MidiDevice> midiDeviceMap = new HashMap<>();
        List<MidiDevice.Info> midiInInfoList = new ArrayList<>();

        //Midi info
        MidiDevice.Info[] midiInfoArr = MidiSystem.getMidiDeviceInfo();

        if (midiInfoArr.length > 0) {
            for (MidiDevice.Info midiInfo : midiInfoArr) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(midiInfo);

                    //Ignore Synthesizers, Sequencers, and devices that aren't receivers
                    if (device instanceof Synthesizer) {
                        //do nothing
                    } else if(device instanceof Sequencer) {
                        //do nothing
                    } else if (device.getMaxTransmitters() != 0) {
                        midiDeviceMap.put(midiInfo.getName(), device);
                        midiInInfoList.add(midiInfo);
                    }
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }

        this.midiInDeviceMap = midiDeviceMap;
        this.midiInDevicesInfo = midiInInfoList;
    }

    private List<String> displayMidiInInfo(){
        List<String> stringList = new ArrayList<>();

        for (MidiDevice.Info midiInfo : midiInDevicesInfo) {
            //Use stringBuilder to create the string we will display
            StringBuilder stringBuilder = new StringBuilder();
            //name of the midi controller
            stringBuilder.append(midiInfo.getName());
            stringBuilder.append(":");
            //description of the midi controller
            stringBuilder.append(midiInfo.getDescription());
            //limit length to 30 chars
            if(stringBuilder.length() > 30){
                stringBuilder.insert(27, "...");
                stringList.add(stringBuilder.substring(0, 30));
            } else {
                stringList.add(stringBuilder.toString());
            }
        }

        return stringList;
    }

    private List<String> displayMidiOutInfo(){
        List<String> stringList = new ArrayList<>();

        for (MidiDevice.Info midiInfo : midiOutDevicesInfo) {
            //Use stringBuilder to create the string we will display
            StringBuilder stringBuilder = new StringBuilder();
            //name of the midi controller
            stringBuilder.append(midiInfo.getName());
            stringBuilder.append(":");
            //description of the midi controller
            stringBuilder.append(midiInfo.getDescription());
            //limit length to 30 chars
            if(stringBuilder.length() > 30){
                stringBuilder.insert(27, "...");
                stringList.add(stringBuilder.substring(0, 30));
            } else {
                stringList.add(stringBuilder.toString());
            }
        }

        return stringList;
    }

    private MidiDevice getMidiInDevice(){
        String midiInName = (String) midiInComboBox.getSelectedItem();
        midiInName = midiInName.split(":")[0];
        return midiInDeviceMap.get(midiInName);
    }

    private MidiDevice getMidiOutDevice(){
        String midiOutName = (String) midiOutComboBox.getSelectedItem();
        midiOutName = midiOutName.split(":")[0];
        return midiOutDeviceMap.get(midiOutName);
    }

    //enables or disables components for midigrid or vgrid
    public void setEnabled(boolean enabled){
        midiInComboBox.setEnabled(enabled);
        midiOutComboBox.setEnabled(enabled);
        chComboBox.setEnabled(enabled);
        dimsRB1.setEnabled(enabled);
        dimsRB2.setEnabled(enabled);
        invertedCheckBox.setEnabled(enabled);
    }

    //returns a hardware device - main method
    public HardwareDevice createHardwareDevice(){

        //open
        HardwareDevice hardwareDevice = new HardwareDevice();

        //open Midi Out and Midi In
        MidiDevice receivingMidiDevice = getMidiOutDevice();
        MidiDevice transmittingMidiDevice = getMidiInDevice();
        try {

            if (!(receivingMidiDevice.isOpen())) {
                receivingMidiDevice.open();
            }

            if (!(transmittingMidiDevice.isOpen())) {
                transmittingMidiDevice.open();
            }

            hardwareDevice = new HardwareDevice(receivingMidiDevice.getReceiver(),
                    transmittingMidiDevice.getTransmitter(),
                    chComboBox.getSelectedIndex());
        } catch (MidiUnavailableException ex) {
            ex.printStackTrace();
        }

        return hardwareDevice;
    }

    public Dimensions getDims(){
        //For now only supporting 64 and 128h grids
        Dimensions dimensions = new Dimensions(8, 16, invertedCheckBox.isSelected());
        if(dimsRB1.isSelected()){
            dimensions.setWidth(8);
        }
        return dimensions;
    }
}
