package com.SitStayCreate.CerealOSC.SysListeners;

import com.SitStayCreate.CerealOSC.MonomeApp.MonomeApp;
import com.SitStayCreate.CerealOSC.MonomeDevice.GridController;
import com.SitStayCreate.Constants;

import com.illposed.osc.*;
import com.illposed.osc.transport.udp.OSCPortOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SysInfoListener implements OSCMessageListener {

    //The controller associated with the listener will never change once it's created
    private final GridController MONOME_CONTROLLER;

    public SysInfoListener(GridController monomeController){
        this.MONOME_CONTROLLER = monomeController;
    }

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        System.out.println("SysInfo Message Received");
        List oscArgs = event.getMessage().getArguments();
        String hostName;
        int portName;
        // /sys/info si <host> <port>
        if(oscArgs.size() == 2){
            hostName = (String) oscArgs.get(0);
            portName = (int) oscArgs.get(1);
            sendInfo(new MonomeApp(hostName, portName));
        // /sys/info i <port>
        } else if(oscArgs.size() == 1){
            hostName = MONOME_CONTROLLER.getMonomeApp().getHostName();
            portName = (int) oscArgs.get(0);
            sendInfo(new MonomeApp(hostName, portName));
        // /sys/info
        } else {
            MonomeApp monomeApp = MONOME_CONTROLLER.getMonomeApp();
            sendInfo(monomeApp);
        }
    }

    // /sys/info si
    private void sendInfo(MonomeApp monomeApp){
        List<OSCMessage> oscMessages = new ArrayList<>();

        //create OSCMessages
        List sysPortArgs = new ArrayList();
        sysPortArgs.add(monomeApp.getPortNumber());
        OSCMessage sysPort = new OSCMessage(Constants.SYS_PORT_MESSAGE, sysPortArgs, new OSCMessageInfo(Constants.SYS_PORT_TYPE_TAG));
        oscMessages.add(sysPort);

        List sysHostArgs = new ArrayList();
        sysHostArgs.add(monomeApp.getHostName());
        OSCMessage sysHost = new OSCMessage(Constants.SYS_HOST_MESSAGE, sysHostArgs, new OSCMessageInfo(Constants.SYS_HOST_TYPE_TAG));
        oscMessages.add(sysHost);

        List sysIdArgs = new ArrayList();
        sysIdArgs.add(MONOME_CONTROLLER.getId());
        OSCMessage sysId = new OSCMessage(Constants.SYS_ID_MESSAGE, sysIdArgs, new OSCMessageInfo(Constants.SYS_ID_TYPE_TAG));
        oscMessages.add(sysId);

        List sysPrefixArgs = new ArrayList();
        sysPrefixArgs.add(MONOME_CONTROLLER.getPrefix());
        OSCMessage sysPrefix = new OSCMessage(Constants.SYS_PREFIX_MESSAGE, sysPrefixArgs, new OSCMessageInfo(Constants.SYS_PREFIX_TYPE_TAG));
        oscMessages.add(sysPrefix);

        //Rotating the controller isn't supported, so this is hardcoded to 0
        List sysRotationArgs = new ArrayList();
        sysRotationArgs.add(0);
        OSCMessage sysRotation = new OSCMessage(Constants.SYS_ROTATION_MESSAGE, sysRotationArgs, new OSCMessageInfo(Constants.SYS_ROTATION_TYPE_TAG));
        oscMessages.add(sysRotation);

        List sysSizeArgs = new ArrayList();
        sysSizeArgs.add(MONOME_CONTROLLER.getDimensions().getWidth());
        sysSizeArgs.add(MONOME_CONTROLLER.getDimensions().getHeight());

        OSCMessage sysSize = new OSCMessage(Constants.SYS_SIZE_MESSAGE, sysSizeArgs, new OSCMessageInfo(Constants.SYS_SIZE_TYPE_TAG));
        oscMessages.add(sysSize);

        try{
            //create a temp port out to send a response
            OSCPortOut tempOscPortOut = new OSCPortOut(monomeApp.getInetAddress(), monomeApp.getPortNumber());
            for(OSCMessage oscMessage : oscMessages){
                tempOscPortOut.send(oscMessage);
            }
            tempOscPortOut.disconnect();
            tempOscPortOut.close();
        } catch(IOException ex){
            ex.printStackTrace();
        } catch (OSCSerializeException ex){
            ex.printStackTrace();
        }
    }
}