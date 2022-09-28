package com.SitStayCreate.GUI.ActionListeners;

import com.SitStayCreate.GUI.DTPane;
import com.SitStayCreate.GUI.GridPanel;
import com.SitStayCreate.GUI.MidiPanel;
import com.SitStayCreate.MidiGrid.HardwareDevice;
import com.SitStayCreate.MidiGrid.MidiGridAdapter;
import com.SitStayCreate.Serialosc.*;
import com.SitStayCreate.VirtualGrid.VGridFrame;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SBActionListener implements ActionListener {
    private GridPanel gridPanel;
    private MidiPanel midiPanel;
    private RequestServer requestServer;
    private DTPane devicePane;
    private final static String ERRORLABEL = "Unavailable port";

    public SBActionListener(GridPanel gridPanel,
                            MidiPanel midiPanel,
                            RequestServer requestServer,
                            DTPane devicePane) {
        this.gridPanel = gridPanel;
        this.midiPanel = midiPanel;
        this.requestServer = requestServer;
        this.devicePane = devicePane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Validate portIn is not already in use by another grid or the system
        int portIn = gridPanel.getPortIn();

        List<GridController> controllers = requestServer.getGridControllers();
        //Check that the port is not already in use, if it is, do not create a device
        for(MonomeController controller : controllers){
            if(controller.getDecoratedOSCPortIn().getPortIn() == portIn){
                gridPanel.setErrorLabel(ERRORLABEL);
                return;
            }
        }

        gridPanel.clearErrorLabel();

        //Test to see if user wants a virtual grid or midi grid
        if(gridPanel.isVGrid()){
            new VGridFrame(requestServer, devicePane, portIn);
            return;
        }


        HardwareDevice hardwareDevice = midiPanel.createHardwareDevice();

        //create OscDevice
        try {
            MidiGridAdapter grid = new MidiGridAdapter(new MonomeApp(10000),
                    midiPanel.getDims(),
                    portIn,
                    hardwareDevice);

            //give the request server a reference to the oscDevice to serve to apps
            requestServer.addMonomeController(grid);
            //Notify apps that a new device exists
            requestServer.notifyListeners(grid);

            devicePane.addRow(grid);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
