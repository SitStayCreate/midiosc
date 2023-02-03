package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDAllListener implements OSCMessageListener {
    @Override
    public void acceptMessage(OSCMessageEvent event) {
        List oscArgList = event.getMessage().getArguments();
        int s = (int) oscArgList.get(0);

        setLedAllState(s);

    }

    public abstract void setLedAllState(int s);
}