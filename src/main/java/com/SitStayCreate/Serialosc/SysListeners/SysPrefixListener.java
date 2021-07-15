package com.SitStayCreate.Serialosc.SysListeners;

import com.SitStayCreate.Serialosc.MonomeController;
import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

public class SysPrefixListener implements OSCMessageListener {

    private final MonomeController MONOMECONTROLLER;

    public SysPrefixListener(MonomeController monomeController){
        this.MONOMECONTROLLER = monomeController;
    }

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        System.out.println((String) event.getMessage().getArguments().get(0));
        MONOMECONTROLLER.setPrefix((String) event.getMessage().getArguments().get(0));
    }
}