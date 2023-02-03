package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDSetListener implements OSCMessageListener {

    public void acceptMessage(OSCMessageEvent event) {
        List oscArgList = event.getMessage().getArguments();
        System.out.println(oscArgList);
        int gridX = (int) oscArgList.get(0);
        int gridY = (int) oscArgList.get(1);
        int gridZ = (int) oscArgList.get(2);

        setLEDState(gridX, gridY, gridZ);
    }

    public abstract void setLEDState(int x, int y, int z);
}
