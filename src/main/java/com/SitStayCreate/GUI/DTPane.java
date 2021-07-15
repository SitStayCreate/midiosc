package com.SitStayCreate.GUI;

import com.SitStayCreate.MidiGrid.MidiGridAdapter;
import com.SitStayCreate.VirtualGrid.VirtualGridController;

import javax.swing.*;
import java.awt.*;

public class DTPane extends JScrollPane {
    private DevicesTable devicesTable;

    public DTPane(DevicesTable devicesTable) {
        super(devicesTable);
        setMaximumSize(new Dimension(300, 100));
        getViewport().setBackground(Color.GRAY);
        this.devicesTable = devicesTable;
    }

    public void addRow(MidiGridAdapter grid){
        devicesTable.addRow(grid);
        updateUI();
    }

    public void addRow(VirtualGridController grid){
        devicesTable.addRow(grid);
        updateUI();
    }

    public void dropRow(int index){
        //TODO: Remove rows from the table when closing
        devicesTable.remove(index);
    }
}