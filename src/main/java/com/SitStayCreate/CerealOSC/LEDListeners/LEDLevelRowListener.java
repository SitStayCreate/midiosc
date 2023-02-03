package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDLevelRowListener implements OSCMessageListener {
    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //oscArgList should have either 10 or 18 args xOffset, gridY, + number of buttons in col (8 or 16)
        List oscArgList = event.getMessage().getArguments();

        //xOffset is either 0 or 8
        int xOffset = (int) oscArgList.get(0);
        int gridY = (int) oscArgList.get(1);

        for (int i = 2; i < oscArgList.size(); i++) {
            int ledState = (int) oscArgList.get(i);
            setLEDLevelRow(xOffset, gridY, i-2, ledState);
        }
    }

    public abstract void setLEDLevelRow(int xOffset, int gridY, int xCounter, int ledState);
}

