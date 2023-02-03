package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

public abstract class LEDLevelAllListener implements OSCMessageListener {

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //s is a number between 0-255. Less than 5 is off, 5 or greater is on.
        int s = (int) event.getMessage().getArguments().get(0);

        setLEDLevelAll(s);
    }

    public abstract void setLEDLevelAll(int s);
}
