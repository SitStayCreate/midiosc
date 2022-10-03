package com.SitStayCreate.GUI;

import com.SitStayCreate.Serialosc.RequestServer;
import com.SitStayCreate.Constants;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    public MainPanel() {

        //Create the request server when the program opens
        RequestServer requestServer = new RequestServer();
        requestServer.startServer();

        GridBagLayout bagLayout = new GridBagLayout();
        setLayout(bagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        //Midi settings
        MidiPanel midiPanel = new MidiPanel(Constants.FONT_SIZE);
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        bagLayout.setConstraints(midiPanel, constraints);
        add(midiPanel);

        //Creates grids
        //need a reference to the DTPane so we can pass it in
        DTPane pane = new DTPane(new DevicesTable(Constants.FONT_SIZE));

        GridPanel gridPanel = new GridPanel(midiPanel, Constants.FONT_SIZE, requestServer, pane);
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        bagLayout.setConstraints(gridPanel, constraints);
        add(gridPanel);

        //Table displays grids - pane created above
        constraints.insets = new Insets(10, 0, 0, 0);
        constraints.insets = new Insets(10, 0, 0, 0);
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(pane, constraints);
    }
}
