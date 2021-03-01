package com.SitStayCreate;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OSCDeviceJPanel extends JPanel {
    //TODO: Filter out the Sequencers and Synthesizers from the midiDevice lists
    //GUI stuff
    private JLabel midiInLabel, midiOutLabel, portInLabel, dimsLabel;
    private JTextField portInTextField;
    private JComboBox<String> midiInComboBox, midiOutComboBox;
    private List<MidiDevice> transmittingMidiDevices, receivingMidiDevices;
    private JCheckBox invertedCheckBox;
    private ButtonGroup sizeRadioButtons;
    private JRadioButton radioButton1, radioButton2;
    private JButton createButton;
    private DefaultTableModel devicesTableModel;

    //Model stuff
    private RequestServer requestServer;
    private OscDevice oscDevice;

    private int idNumber;

    protected OSCDeviceJPanel(){

        int smallFont = 16;
        int bigFont = 18;
        Font labelFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font cboxFont = new Font(Font.MONOSPACED, Font.PLAIN, smallFont);
        Font tfieldFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font rbuttonFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font chkboxFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font buttonFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font tableFont = new Font(Font.MONOSPACED, Font.BOLD, smallFont);
        Font tableHeaderFont = new Font(Font.MONOSPACED, Font.PLAIN, smallFont);

        GridBagLayout bagLayout = new GridBagLayout();
        setLayout(bagLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;


        idNumber = 1;
        //Create the request server when the program opens
        requestServer = new RequestServer();
        requestServer.startServer();

        //Setup the GUI stuff
        transmittingMidiDevices = SelectTransmittingMidiDevices();

        midiInLabel = new JLabel("Midi In: ");
        midiInLabel.setForeground(Color.WHITE);
        midiInLabel.setFont(labelFont);
        c.insets = new Insets(10, 0, 0, 0);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        bagLayout.setConstraints(midiInLabel, c);
        add(midiInLabel);

        midiInComboBox = new JComboBox<>();
        midiInComboBox.setFont(cboxFont);
        for (String midiOutDevice : selectMidiInDevices()) {
            midiInComboBox.addItem(midiOutDevice);
        }
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        bagLayout.setConstraints(midiInComboBox, c);
        add(midiInComboBox);

        midiOutLabel = new JLabel("Midi Out: ");
        midiOutLabel.setForeground(Color.WHITE);
        midiOutLabel.setFont(labelFont);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        bagLayout.setConstraints(midiOutLabel, c);
        add(midiOutLabel);


        midiOutComboBox = new JComboBox<>();
        midiOutComboBox.setFont(cboxFont);
        for(String midiOutDevice : selectMidiOutDevices()){
            midiOutComboBox.addItem(midiOutDevice);
        }
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 1;
        bagLayout.setConstraints(midiOutComboBox, c);
        add(midiOutComboBox);

        portInLabel = new JLabel("Port In: ");
        portInLabel.setForeground(Color.WHITE);
        portInLabel.setFont(labelFont);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        bagLayout.setConstraints(portInLabel, c);
        add(portInLabel);

        portInTextField = new JTextField("8080");
        portInTextField.setFont(tfieldFont);
        c.gridx = 1;
        c.gridy = 2;
        bagLayout.setConstraints(portInTextField, c);
        add(portInTextField);

        dimsLabel = new JLabel("Dimensions: ");
        dimsLabel.setFont(labelFont);
        dimsLabel.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 3;
        bagLayout.setConstraints(dimsLabel, c);
        add(dimsLabel);

        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.DARK_GRAY);
        sizeRadioButtons = new ButtonGroup();

        radioButton1 = new JRadioButton("64");
        radioButton1.setFont(rbuttonFont);
        radioButton1.setForeground(Color.WHITE);
        radioButton1.setBackground(Color.DARK_GRAY);
        radioButton1.setSelected(true);
        sizeRadioButtons.add(radioButton1);
        jPanel.add(radioButton1);

        radioButton2 = new JRadioButton("128h");
        radioButton2.setFont(rbuttonFont);
        radioButton2.setForeground(Color.WHITE);
        radioButton2.setBackground(Color.DARK_GRAY);
        sizeRadioButtons.add(radioButton2);
        jPanel.add(radioButton2);

        c.gridx = 1;
        c.gridy = 3;
        bagLayout.setConstraints(jPanel, c);
        add(jPanel);


        invertedCheckBox = new JCheckBox("Invert Y-Axis");
        invertedCheckBox.setFont(chkboxFont);
        invertedCheckBox.setForeground(Color.WHITE);
        invertedCheckBox.setBackground(Color.DARK_GRAY);
        c.gridx = 2;
        c.gridy = 3;
        bagLayout.setConstraints(invertedCheckBox, c);
        add(invertedCheckBox);

        JPanel sbPanel = new JPanel();
        sbPanel.setLayout(new GridBagLayout());
        sbPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints sbC = new GridBagConstraints();
        sbC.fill = GridBagConstraints.HORIZONTAL;
        sbC.gridx = 0;
        sbC.gridy = 0;

        createButton = new JButton("Create");
        createButton.setFont(buttonFont);
        createButton.setPreferredSize(new Dimension(120, 40));
        createButton.addActionListener(new SetButtonActionListener());

        sbPanel.add(createButton, sbC);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridheight = 2;
        c.gridx = 2;
        c.gridy = 4;
        bagLayout.setConstraints(sbPanel, c);
        add(sbPanel);

        devicesTableModel = new DefaultTableModel();
        devicesTableModel.addColumn("ID");
        devicesTableModel.addColumn("Size");
        devicesTableModel.addColumn("Port In");
        devicesTableModel.addColumn("Inverted");

        JTable devicesTable = new JTable(devicesTableModel);

        devicesTable.getTableHeader().setFont(tableHeaderFont);
        devicesTable.setFont(tableFont);
        devicesTable.setPreferredScrollableViewportSize(new Dimension(300, 60));
        devicesTable.revalidate();
        devicesTable.setBackground(Color.GRAY);
        devicesTable.setForeground(Color.YELLOW);


        JScrollPane pane = new JScrollPane(devicesTable);
        pane.getViewport().setBackground(Color.GRAY);

        c.insets = new Insets(5, 0, 0, 0);
        c.gridheight = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 6;
        add(pane, c);
    }

    public class SetButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            //Validate portIn is not already in use by another grid or the system
            int portNumber = Integer.parseInt(portInTextField.getText());
            List<OscDevice> devices = requestServer.getOscDevices();
            //Check that the port is not already in use, if it is, do not create a device
            for(OscDevice device : devices){
                if(device.getOscPortInWrapper().getPortInNumber() == portNumber){
                    return;
                }
            }

            HardwareDevice hardwareDevice = new HardwareDevice();

            //open Midi In
            //Midi Stuff that needs to be refactored
            int index = midiInComboBox.getSelectedIndex();
            try {
                MidiDevice transmittingMidiDevice = transmittingMidiDevices.get(index);

                if (!(transmittingMidiDevice.isOpen())) {
                    transmittingMidiDevice.open();
                }

                hardwareDevice.setTransmittingMidiDevice(transmittingMidiDevice);
            } catch (MidiUnavailableException ex){
                ex.printStackTrace();
            }

            //open Midi Out
            receivingMidiDevices = SelectReceivingMidiDevices();
            int index2 = midiOutComboBox.getSelectedIndex();
            MidiDevice receivingMidiDevice = receivingMidiDevices.get(index2);
            try {
                if (!(receivingMidiDevice.isOpen())) {

                    receivingMidiDevice.open();
                }
                hardwareDevice.setReceivingMidiDevice(receivingMidiDevice);
            } catch (MidiUnavailableException ex) {
                ex.printStackTrace();
            }

            //For now only supporting 64 and 128h grids
            Dimensions dimensions = new Dimensions(8, 16, invertedCheckBox.isSelected());
            if(radioButton1.isSelected()){
                dimensions.setWidth(8);
            }

            //create OscDevice
            oscDevice = new OscDevice(dimensions,
                                    portNumber,
                                    String.format("ssc-0000%d", idNumber));

            idNumber++;

            //Virtual Device + Physical Device joined
            oscDevice.setHardwareDevice(hardwareDevice);

            //give the request server a reference to the oscDevice to serve to apps
            requestServer.addOscDevice(oscDevice);
            //Notify apps that a new device exists
            requestServer.notifyListeners(oscDevice);

            //Add device to the deviceTable
            Object[] rowData = {oscDevice.getId(),
                    oscDevice.getDimensions().getWidth() + "x" + oscDevice.getDimensions().getHeight(),
                    oscDevice.getOscPortInWrapper().getPortInNumber(),
                    oscDevice.getDimensions().isInverted()};
            devicesTableModel.addRow(rowData);
        }
    }

    private ArrayList<String> selectMidiOutDevices(){
        MidiDevice.Info[] midiDeviceInfoArray = MidiSystem.getMidiDeviceInfo();
        ArrayList<MidiDevice.Info> receivingMidiDeviceInfoArrayList = new ArrayList<>();

        if (midiDeviceInfoArray.length > 0) {
            for (MidiDevice.Info info : midiDeviceInfoArray) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(info);

                    if (device instanceof Synthesizer) {
                        //do nothing
                    } else if(device instanceof Sequencer) {
                        //do nothing
                    } else if (device.getMaxReceivers() != 0) {
                        receivingMidiDeviceInfoArrayList.add(info);
                    }

                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList<String> midiNameArraylist = new ArrayList<>();
        for (MidiDevice.Info midiInfo : receivingMidiDeviceInfoArrayList){
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
                midiNameArraylist.add(stringBuilder.substring(0, 30));
            } else {
                midiNameArraylist.add(stringBuilder.toString());
            }
        }

        return midiNameArraylist;
    }

    private List<MidiDevice> SelectReceivingMidiDevices() {
        MidiDevice.Info[] midiDeviceInfoArray = MidiSystem.getMidiDeviceInfo();
        ArrayList<MidiDevice> receivingMidiDeviceArrayList = new ArrayList<>();

        if (midiDeviceInfoArray.length > 0) {
            for (MidiDevice.Info info : midiDeviceInfoArray) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(info);

                    if (device instanceof Synthesizer) {
                        //do nothing
                    } else if(device instanceof Sequencer) {
                        //do nothing
                    } else if (device.getMaxReceivers() != 0) {
                        receivingMidiDeviceArrayList.add(device);
                    }

                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }

        return receivingMidiDeviceArrayList;

    }

    private ArrayList<String> selectMidiInDevices() {
        MidiDevice.Info[] midiDeviceInfoArray = MidiSystem.getMidiDeviceInfo();
        ArrayList<MidiDevice.Info> transmittingMidiDeviceInfoArrayList = new ArrayList<>();

        if (midiDeviceInfoArray.length > 0) {
            for (MidiDevice.Info info : midiDeviceInfoArray) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(info);

                    if (device instanceof Synthesizer) {
                        //do nothing
                    } else if(device instanceof Sequencer) {
                        //do nothing
                    } else if (device.getMaxTransmitters() != 0) {
                        transmittingMidiDeviceInfoArrayList.add(info);
                    }
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList<String> midiNameArraylist = new ArrayList<>();
        for (MidiDevice.Info midiInfo : transmittingMidiDeviceInfoArrayList) {
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
                midiNameArraylist.add(stringBuilder.substring(0, 30));
            } else {
                midiNameArraylist.add(stringBuilder.toString());
            }
        }

        return midiNameArraylist;
    }

    private List<MidiDevice> SelectTransmittingMidiDevices() {
        MidiDevice.Info[] midiDeviceInfoArray = MidiSystem.getMidiDeviceInfo();
        ArrayList<MidiDevice> transmittingMidiDeviceArrayList = new ArrayList<>();

        if (midiDeviceInfoArray.length > 0) {
            for (MidiDevice.Info info : midiDeviceInfoArray) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(info);

                    if (device instanceof Synthesizer) {
                        //do nothing
                    } else if(device instanceof Sequencer) {
                        //do nothing
                    } else if (device.getMaxTransmitters() != 0) {
                        transmittingMidiDeviceArrayList.add(device);
                    }

                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }

        return transmittingMidiDeviceArrayList;

    }

}
