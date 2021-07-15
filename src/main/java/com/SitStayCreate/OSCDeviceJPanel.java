package com.SitStayCreate;

import com.SitStayCreate.MidiGrid.HardwareDevice;
import com.SitStayCreate.MidiGrid.MidiGridAdapter;
import com.SitStayCreate.Serialosc.*;
import com.SitStayCreate.VirtualGrid.VGButton;
import com.SitStayCreate.VirtualGrid.VirtualGridController;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OSCDeviceJPanel extends JPanel {
    //GUI stuff
    private JLabel midiInLabel, midiOutLabel, portInLabel, dimsLabel, typeLabel, errorLabel;
    private JTextField portInTextField;
    private JComboBox<String> midiInComboBox, midiOutComboBox;
    private List<MidiDevice> transmittingMidiDevices, receivingMidiDevices;
    private JCheckBox invertedCheckBox;
    private ButtonGroup sizeRadioButtons, typeRadioButtons;
    private JRadioButton radioButton1, radioButton2, typeRB1, typeRB2;
    private JButton createButton;
    private DefaultTableModel devicesTableModel;

    //Model stuff
    private RequestServer requestServer;
    private GridController monomeController;

    public OSCDeviceJPanel(){

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
        c.insets = new Insets(10, 0, 0, 0);

        //Create the request server when the program opens
        requestServer = new RequestServer();
        requestServer.startServer();

        //Setup the GUI stuff
        transmittingMidiDevices = SelectTransmittingMidiDevices();

        //MIDI In
        JPanel subPanel0 = new JPanel();
        subPanel0.setBackground(Color.DARK_GRAY);
        GridBagLayout subLayout0 = new GridBagLayout();
        subPanel0.setLayout(subLayout0);
        GridBagConstraints subC0 = new GridBagConstraints();
        subC0.insets = new Insets(0, 0, 5, 0);
        subC0.fill = GridBagConstraints.HORIZONTAL;

        midiInLabel = new JLabel("Midi In: ");
        midiInLabel.setForeground(Color.WHITE);
        midiInLabel.setFont(labelFont);
        subC0.gridx = 0;
        subC0.gridy = 0;
        subLayout0.setConstraints(midiInLabel, subC0);
        subPanel0.add(midiInLabel);

        midiInComboBox = new JComboBox<>();
        midiInComboBox.setFont(cboxFont);
        for (String midiOutDevice : selectMidiInDevices()) {
            midiInComboBox.addItem(midiOutDevice);
        }
        subC0.gridwidth = 3;
        subC0.gridx = 1;
        subC0.gridy = 0;
        subLayout0.setConstraints(midiInComboBox, subC0);
        subPanel0.add(midiInComboBox);

        //MIDI Out
        midiOutLabel = new JLabel("Midi Out: ");
        midiOutLabel.setForeground(Color.WHITE);
        midiOutLabel.setFont(labelFont);
        subC0.gridwidth = 1;
        subC0.gridx = 0;
        subC0.gridy = 1;
        subLayout0.setConstraints(midiOutLabel, subC0);
        subPanel0.add(midiOutLabel);


        midiOutComboBox = new JComboBox<>();
        midiOutComboBox.setFont(cboxFont);
        for(String midiOutDevice : selectMidiOutDevices()){
            midiOutComboBox.addItem(midiOutDevice);
        }
        subC0.gridwidth = 3;
        subC0.gridx = 1;
        subC0.gridy = 1;
        subLayout0.setConstraints(midiOutComboBox, subC0);
        subPanel0.add(midiOutComboBox);

        //OSC Port In
        portInLabel = new JLabel("Port In: ");
        portInLabel.setForeground(Color.WHITE);
        portInLabel.setFont(labelFont);
        subC0.gridwidth = 1;
        subC0.gridx = 0;
        subC0.gridy = 2;
        subLayout0.setConstraints(portInLabel, subC0);
        subPanel0.add(portInLabel);

        portInTextField = new JTextField("8080");
        portInTextField.setFont(tfieldFont);
        portInTextField.setColumns(6);
        subC0.gridx = 1;
        subC0.gridy = 2;
        subC0.gridwidth = 1;
        subLayout0.setConstraints(portInTextField, subC0);
        subPanel0.add(portInTextField);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(labelFont);
        subC0.gridx = 2;
        subC0.gridy = 2;
        subC0.gridwidth = 2;
        subC0.insets = new Insets(0, 10, 5, 0);
        subLayout0.setConstraints(errorLabel, subC0);
        subPanel0.add(errorLabel);

        //Dimensions

        dimsLabel = new JLabel("Dimens: ");
        dimsLabel.setFont(labelFont);
        dimsLabel.setForeground(Color.WHITE);
        subC0.gridx = 0;
        subC0.gridy = 3;
        subC0.gridwidth = 1;
        subC0.insets = new Insets(0, 0, 5, 0);
        subLayout0.setConstraints(dimsLabel, subC0);
        subPanel0.add(dimsLabel, subC0);


        sizeRadioButtons = new ButtonGroup();

        radioButton1 = new JRadioButton("64");
        radioButton1.setFont(rbuttonFont);
        radioButton1.setForeground(Color.WHITE);
        radioButton1.setBackground(Color.DARK_GRAY);
        radioButton1.setSelected(true);
        subC0.gridx = 1;
        subC0.gridy = 3;
        subLayout0.setConstraints(radioButton1, subC0);
        sizeRadioButtons.add(radioButton1);
        subPanel0.add(radioButton1, subC0);

        radioButton2 = new JRadioButton("128h");
        radioButton2.setFont(rbuttonFont);
        radioButton2.setForeground(Color.WHITE);
        radioButton2.setBackground(Color.DARK_GRAY);
        subC0.gridx = 2;
        subC0.gridy = 3;
        subLayout0.setConstraints(radioButton2, subC0);
        sizeRadioButtons.add(radioButton2);
        subPanel0.add(radioButton2, subC0);

        invertedCheckBox = new JCheckBox("Invert Y-Axis");
        invertedCheckBox.setFont(chkboxFont);
        invertedCheckBox.setForeground(Color.WHITE);
        invertedCheckBox.setBackground(Color.DARK_GRAY);
        subC0.gridx = 3;
        subC0.gridy = 3;
        subLayout0.setConstraints(invertedCheckBox, subC0);
        subPanel0.add(invertedCheckBox, subC0);

        //Grid Type
        typeLabel = new JLabel("Type: ");
        typeLabel.setFont(labelFont);
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setBackground(Color.DARK_GRAY);
        subC0.gridx = 0;
        subC0.gridy = 4;
        subLayout0.setConstraints(typeLabel, subC0);
        subPanel0.add(typeLabel, subC0);

        typeRadioButtons = new ButtonGroup();

        typeRB1 = new JRadioButton("Midi");
        typeRB1.setFont(rbuttonFont);
        typeRB1.setForeground(Color.WHITE);
        typeRB1.setBackground(Color.DARK_GRAY);
        typeRB1.setSelected(true);
        typeRB1.addActionListener(new TypeRB1ActionListener());
        typeRadioButtons.add(typeRB1);
        subC0.gridx = 1;
        subC0.gridy = 4;
        subLayout0.setConstraints(typeRB1, subC0);
        subPanel0.add(typeRB1, subC0);

        typeRB2 = new JRadioButton("VGrid");
        typeRB2.setFont(rbuttonFont);
        typeRB2.setForeground(Color.WHITE);
        typeRB2.setBackground(Color.DARK_GRAY);
        typeRB2.addActionListener(new TypeRB2ActionListener());
        typeRadioButtons.add(typeRB2);
        subC0.gridx = 2;
        subC0.gridy = 4;
        subLayout0.setConstraints(typeRB2, subC0);
        subPanel0.add(typeRB2, subC0);

        //Create Button
        createButton = new JButton("Create");
        createButton.setFont(buttonFont);
        createButton.setPreferredSize(new Dimension(120, 40));
        createButton.addActionListener(new SBActionListener());
        subC0.gridx = 3;
        subC0.gridy = 4;
        subLayout0.setConstraints(createButton, subC0);
        subPanel0.add(createButton, subC0);

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 5;
        c.gridwidth = 3;

        bagLayout.setConstraints(subPanel0, c);
        add(subPanel0, c);

        c.gridx = 0;
        c.gridy = 5;

        devicesTableModel = new DefaultTableModel();
        devicesTableModel.addColumn("ID");
        devicesTableModel.addColumn("Size");
        devicesTableModel.addColumn("Port In");
        devicesTableModel.addColumn("Inverted");

        JTable devicesTable = new JTable(devicesTableModel);
        devicesTable.setEnabled(false);

        devicesTable.getTableHeader().setFont(tableHeaderFont);
        devicesTable.setFont(tableFont);
        devicesTable.setPreferredScrollableViewportSize(new Dimension(300, 60));
        devicesTable.revalidate();
        devicesTable.setBackground(Color.GRAY);
        devicesTable.setForeground(Color.YELLOW);


        JScrollPane pane = new JScrollPane(devicesTable);
        pane.getViewport().setBackground(Color.GRAY);

        c.insets = new Insets(10, 0, 0, 0);
        c.gridheight = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 6;
        add(pane, c);
    }

    public class TypeRB1ActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            midiInComboBox.setEnabled(true);
            midiOutComboBox.setEnabled(true);
            radioButton1.setEnabled(true);
            radioButton2.setEnabled(true);
            invertedCheckBox.setEnabled(true);
        }
    }

    public class TypeRB2ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            midiInComboBox.setEnabled(false);
            midiOutComboBox.setEnabled(false);
            radioButton1.setEnabled(false);
            radioButton2.setEnabled(false);
            invertedCheckBox.setEnabled(false);
        }
    }

    public class SBActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            //Validate portIn is not already in use by another grid or the system
            int portNumber = Integer.parseInt(portInTextField.getText());

            List<GridController> controllers = requestServer.getGridControllers();
            //Check that the port is not already in use, if it is, do not create a device
            for(MonomeController controller : controllers){
                if(controller.getDecoratedOSCPortIn().getPortIn() == portNumber){
                    errorLabel.setText("Unavailable port");
                    errorLabel.setVisible(true);
                    return;
                }
            }

            errorLabel.setVisible(false);

            HardwareDevice hardwareDevice = new HardwareDevice();

            //open Midi In
            //Midi Stuff that needs to be refactored
            int index = midiInComboBox.getSelectedIndex();
            try {
                MidiDevice transmittingMidiDevice = transmittingMidiDevices.get(index);

                if (!(transmittingMidiDevice.isOpen())) {
                    transmittingMidiDevice.open();
                }

                hardwareDevice.setTransmitter(transmittingMidiDevice.getTransmitter());
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
                hardwareDevice.setReceiver(receivingMidiDevice.getReceiver());
            } catch (MidiUnavailableException ex) {
                ex.printStackTrace();
            }

            //Test to see if user wants a virtual grid or midi grid
            if(typeRB2.isSelected()){
                int portIn = Integer.parseInt(portInTextField.getText());
                goVirtualGrid(portIn);
                return;
            }

            //For now only supporting 64 and 128h grids
            Dimensions dimensions = new Dimensions(8, 16, invertedCheckBox.isSelected());
            if(radioButton1.isSelected()){
                dimensions.setWidth(8);
            }

            //create OscDevice
            try {
                monomeController = new MidiGridAdapter(new MonomeApp(10000),
                        dimensions,
                        portNumber,
                        hardwareDevice);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //give the request server a reference to the oscDevice to serve to apps
            requestServer.addMonomeController(monomeController);
            //Notify apps that a new device exists
            requestServer.notifyListeners(monomeController);

            //Add device to the deviceTable
            Object[] rowData = {monomeController.getId(),
                    ((MidiGridAdapter) monomeController).getDimensions().getWidth() + "x" +
                            ((MidiGridAdapter) monomeController).getDimensions().getHeight(),
                    monomeController.getDecoratedOSCPortIn().getPortIn(),
                    ((MidiGridAdapter) monomeController).getDimensions().isInverted()};
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

    public void goVirtualGrid(int portInNumber){
        //TODO: Replace null with NullController
        VirtualGridController virtualGridController = null;
        try {
            virtualGridController = new VirtualGridController(portInNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //give the request server a reference to the controller to serve to apps
        requestServer.addMonomeController(virtualGridController);
        //Notify apps that a new device exists
        requestServer.notifyListeners(virtualGridController);

        //Add device to the deviceTable
        Object[] rowData = {virtualGridController.getId(),
                virtualGridController.getDimensions().getWidth() + "x" +
                        virtualGridController.getDimensions().getHeight(),
                virtualGridController.getDecoratedOSCPortIn().getPortIn(),
                virtualGridController.getDimensions().isInverted()};
        devicesTableModel.addRow(rowData);

        JFrame jFrame = new JFrame(virtualGridController.getId());

        JPanel buttonPanel = new JPanel();
        GridBagLayout bpLayout = new GridBagLayout();
        buttonPanel.setLayout(bpLayout);

        GridBagConstraints bpC = new GridBagConstraints();

        bpC.fill = GridBagConstraints.HORIZONTAL;
        bpC.insets = new Insets(5, 5, 5, 5);

        for (int j = 0; j < virtualGridController.getDimensions().getHeight(); j++){
            for (int i = 0; i < virtualGridController.getDimensions().getWidth(); i++){
                VGButton button = virtualGridController.getButtonMatrix()[i][j];
                bpC.gridx = i;
                bpC.gridy = j;
                bpLayout.setConstraints(button, bpC);
                buttonPanel.add(button);
            }
        }
        jFrame.add(buttonPanel);
        jFrame.setSize(1200, 650);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
