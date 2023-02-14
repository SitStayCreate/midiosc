package com.SitStayCreate.CerealOSC.SysListeners;

import com.SitStayCreate.CerealOSC.MonomeApp.MonomeApp;
import com.SitStayCreate.CerealOSC.MonomeDevice.MonomeController;
import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.io.IOException;

// This class updates the hostName of a Monome App
public class SysHostListener implements OSCMessageListener {

    private final MonomeController MONOMECONTROLLER;

    public SysHostListener(MonomeController monomeController){
        this.MONOMECONTROLLER = monomeController;
    }

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        System.out.println("SysHost Message Received");
        String hostName = (String) event.getMessage().getArguments().get(0);
        System.out.println("Host name: " + hostName);
        //Update the target monomeApp
        MonomeApp currentApp = MONOMECONTROLLER.getMonomeApp();
        //change the hostName, but use the same portNumber
        MonomeApp newApp = new MonomeApp(hostName, currentApp.getPortNumber());
        try {
            MONOMECONTROLLER.setMonomeApp(newApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
