package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDLevelMapListener implements OSCMessageListener {

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        List oscList = event.getMessage().getArguments();
        setLEDLevelMap(oscList);
    }

    public abstract void setLEDLevelMap(List oscList);
}
