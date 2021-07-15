package com.SitStayCreate.VirtualGrid;

import com.SitStayCreate.GUI.DTPane;
import com.SitStayCreate.Serialosc.Dimensions;
import com.SitStayCreate.Serialosc.RequestServer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class VGridFrame extends JFrame {

    //TODO: Work on this
    public VGridFrame(RequestServer requestServer, DTPane devicePane, int portInNumber){
        //TODO: Consider replacing with NullController
        VirtualGridController vgrid = null;
        try {
            vgrid = new VirtualGridController(portInNumber, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //give the request server a reference to the controller to serve to apps
        requestServer.addMonomeController(vgrid);
        //Notify apps that a new device exists
        requestServer.notifyListeners(vgrid);
        devicePane.addRow(vgrid);
        setTitle(vgrid.getId());

        JPanel buttonPanel = new JPanel();
        GridBagLayout bpLayout = new GridBagLayout();
        buttonPanel.setLayout(bpLayout);

        GridBagConstraints bpC = new GridBagConstraints();

        bpC.fill = GridBagConstraints.HORIZONTAL;
        bpC.insets = new Insets(5, 5, 5, 5);

        Dimensions vgridDims = vgrid.getDimensions();

        for (int j = 0; j < vgridDims.getHeight(); j++){
            for (int i = 0; i < vgridDims.getWidth(); i++){
                VGButton button = vgrid.getButtonMatrix()[i][j];
                bpC.gridx = i;
                bpC.gridy = j;
                bpLayout.setConstraints(button, bpC);
                buttonPanel.add(button);
            }
        }
        add(buttonPanel);
        setSize(1200, 650);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
