package com.SitStayCreate.Serialosc.SysListeners;

import com.SitStayCreate.Serialosc.MonomeApp;
import com.SitStayCreate.Serialosc.GridController;

import com.illposed.osc.*;
import com.illposed.osc.transport.udp.OSCPortOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SysInfoListener implements OSCMessageListener {

    //The controller associated with the listener will never change once it's created
    private final GridController MONOMECONTROLLER;

    public SysInfoListener(GridController monomeController){
        this.MONOMECONTROLLER = monomeController;
    }

    @Override
    public void acceptMessage(OSCMessageEvent event) {
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
            hostName = MONOMECONTROLLER.getMonomeApp().getHostName();
            portName = (int) oscArgs.get(0);
            sendInfo(new MonomeApp(hostName, portName));
        // /sys/info
        } else {
            MonomeApp monomeApp = MONOMECONTROLLER.getMonomeApp();
            sendInfo(monomeApp);
        }
    }

    // /sys/info si
    private void sendInfo(MonomeApp monomeApp){
        List<OSCMessage> oscMessages = new ArrayList<>();

        //create OSCMessages
        List sysPortArgs = new ArrayList();
        sysPortArgs.add(monomeApp.getPortNumber());
        OSCMessage sysPort = new OSCMessage("/sys/port", sysPortArgs, new OSCMessageInfo("i"));
        oscMessages.add(sysPort);

        List sysHostArgs = new ArrayList();
        sysHostArgs.add(monomeApp.getHostName());
        OSCMessage sysHost = new OSCMessage("/sys/host", sysHostArgs, new OSCMessageInfo("s"));
        oscMessages.add(sysHost);

        List sysIdArgs = new ArrayList();
        sysIdArgs.add(MONOMECONTROLLER.getId());
        OSCMessage sysId = new OSCMessage("/sys/id", sysIdArgs, new OSCMessageInfo("s"));
        oscMessages.add(sysId);

        List sysPrefixArgs = new ArrayList();
        sysPrefixArgs.add(MONOMECONTROLLER.getPrefix());
        OSCMessage sysPrefix = new OSCMessage("/sys/prefix", sysPrefixArgs, new OSCMessageInfo("s"));
        oscMessages.add(sysPrefix);

        //Rotating the controller isn't supported, so this is hardcoded to 0
        List sysRotationArgs = new ArrayList();
        sysRotationArgs.add(0);
        OSCMessage sysRotation = new OSCMessage("/sys/rotation", sysRotationArgs, new OSCMessageInfo("i"));
        oscMessages.add(sysRotation);

        List sysSizeArgs = new ArrayList();
        sysSizeArgs.add(MONOMECONTROLLER.getDimensions().getWidth());
        sysSizeArgs.add(MONOMECONTROLLER.getDimensions().getHeight());

        OSCMessage sysSize = new OSCMessage("/sys/size", sysSizeArgs, new OSCMessageInfo("ii"));
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