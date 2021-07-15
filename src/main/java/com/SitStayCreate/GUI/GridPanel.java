package com.SitStayCreate.GUI;

import com.SitStayCreate.GUI.ActionListeners.SBActionListener;
import com.SitStayCreate.GUI.ActionListeners.TypeRB1ActionListener;
import com.SitStayCreate.GUI.ActionListeners.TypeRB2ActionListener;
import com.SitStayCreate.Serialosc.RequestServer;

import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {

    private JLabel portInLabel, errorLabel;
    private JTextField portInTextField;
    private JLabel typeLabel;
    private ButtonGroup typeRadioButtons;
    private JRadioButton typeRB1, typeRB2;
    private JButton createButton;
    private RequestServer requestServer;

    public GridPanel(MidiPanel midiPanel,
                     int font,
                     RequestServer requestServer,
                     DTPane devicePane) {
        this.requestServer = requestServer;
        int bigFont = font;

        Font labelFont = new Font(Font.MONOSPACED, Font.PLAIN, font);
        Font rbuttonFont = new Font(Font.MONOSPACED, Font.PLAIN, font);
        Font tfieldFont = new Font(Font.MONOSPACED, Font.PLAIN, bigFont);
        Font buttonFont = new Font(Font.MONOSPACED, Font.PLAIN, font);

        setBackground(Color.DARK_GRAY);
        GridBagLayout bagLayout = new GridBagLayout();
        setLayout(bagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 5, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        //OSC Port In
        portInLabel = new JLabel("Port In:  ");
        portInLabel.setForeground(Color.WHITE);
        portInLabel.setFont(labelFont);
        constraints.gridx = 0;
        constraints.gridy = 0;
        bagLayout.setConstraints(portInLabel, constraints);
        add(portInLabel);

        portInTextField = new JTextField("8080");
        portInTextField.setFont(tfieldFont);
        portInTextField.setColumns(6);
        constraints.gridx = 1;
        constraints.gridy = 0;
        bagLayout.setConstraints(portInTextField, constraints);
        add(portInTextField);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(labelFont);
        constraints.gridwidth = 2;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 5, 0);
        bagLayout.setConstraints(errorLabel, constraints);
        add(errorLabel);

        //Grid Type
        typeLabel = new JLabel("Type: ");
        typeLabel.setFont(labelFont);
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setBackground(Color.DARK_GRAY);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        bagLayout.setConstraints(typeLabel, constraints);
        add(typeLabel);

        typeRadioButtons = new ButtonGroup();

        typeRB1 = new JRadioButton("Midi");
        typeRB1.setFont(rbuttonFont);
        typeRB1.setForeground(Color.WHITE);
        typeRB1.setBackground(Color.DARK_GRAY);
        typeRB1.setSelected(true);
        typeRB1.addActionListener(new TypeRB1ActionListener(midiPanel, this));
        typeRadioButtons.add(typeRB1);
        constraints.gridx = 1;
        constraints.gridy = 1;
        bagLayout.setConstraints(typeRB1, constraints);
        add(typeRB1);

        typeRB2 = new JRadioButton("VGrid");
        typeRB2.setFont(rbuttonFont);
        typeRB2.setForeground(Color.WHITE);
        typeRB2.setBackground(Color.DARK_GRAY);
        typeRB2.addActionListener(new TypeRB2ActionListener(midiPanel, this));
        typeRadioButtons.add(typeRB2);
        constraints.gridx = 2;
        constraints.gridy = 1;
        bagLayout.setConstraints(typeRB2, constraints);
        add(typeRB2);


        //Create Button
        createButton = new JButton("Create");
        createButton.setFont(buttonFont);
        createButton.setPreferredSize(new Dimension(120, 40));
        createButton.addActionListener(new SBActionListener(this,
                midiPanel,
                this.requestServer,
                devicePane));
        constraints.gridx = 3;
        constraints.gridy = 1;
        bagLayout.setConstraints(createButton, constraints);
        add(createButton);

    }

    public int getPortIn(){
        return Integer.parseInt(portInTextField.getText());
    }

    //TODO: more getters for getting data from components

    public void setErrorLabel(String text){
        errorLabel.setText(text);
        errorLabel.setVisible(true);
    }

    public void clearErrorLabel(){
        errorLabel.setVisible(false);
    }

    public boolean isVGrid(){
        return typeRB2.isSelected();
    }
}
