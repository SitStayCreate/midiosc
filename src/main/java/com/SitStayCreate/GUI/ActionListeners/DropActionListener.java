package com.SitStayCreate.GUI.ActionListeners;

import com.SitStayCreate.GUI.DevicesTable;
import com.SitStayCreate.Serialosc.MonomeController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DropActionListener implements ActionListener {

    private MonomeController monomeController;
    private DevicesTable devicesTable;

    public DropActionListener(DevicesTable devicesTable, MonomeController monomeController) {
        this.monomeController = monomeController;
        this.devicesTable = devicesTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO: Delete grids from the Server, notify targetApps, free up grid resources,
        // remove from DevicesTable
        monomeController.close();
        devicesTable.dropRow(monomeController.getId());
    }
}
