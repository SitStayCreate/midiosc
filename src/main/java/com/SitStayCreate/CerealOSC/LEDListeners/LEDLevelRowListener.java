package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDLevelRowListener implements OSCMessageListener {
    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //oscArgList should have either 10 or 18 args xOffset, gridY, + number of buttons in col (8 or 16)
        List oscArgList = event.getMessage().getArguments();
        setLEDLevelRow(oscArgList);
    }

    public abstract void setLEDLevelRow(List oscList);
}