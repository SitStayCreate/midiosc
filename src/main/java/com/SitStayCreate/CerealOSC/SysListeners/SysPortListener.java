package com.SitStayCreate.CerealOSC.SysListeners;

import com.SitStayCreate.CerealOSC.MonomeApp.MonomeApp;
import com.SitStayCreate.CerealOSC.MonomeDevice.MonomeController;
import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.io.IOException;
import java.net.UnknownHostException;

public class SysPortListener implements OSCMessageListener {

    private final MonomeController MONOMECONTROLLER;

    public SysPortListener(MonomeController monomeController){
        this.MONOMECONTROLLER = monomeController;
    }

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        System.out.println("SysPort Message Received");
        int targetPort = (int) event.getMessage().getArguments().get(0);
        System.out.println("targetPort: " + targetPort);
        try {
            //Update the target monomeApp
            MonomeApp monomeApp = new MonomeApp(targetPort);
            MONOMECONTROLLER.setMonomeApp(monomeApp);
        } catch (UnknownHostException uhex){
            uhex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
}