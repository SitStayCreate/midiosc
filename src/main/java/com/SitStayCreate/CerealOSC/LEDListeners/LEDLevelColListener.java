package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDLevelColListener implements OSCMessageListener {

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //oscArgList should have either 10 or 18 args xOffset, gridY, + number of buttons in col (8 or 16)
        List oscArgList = event.getMessage().getArguments();

        int gridX = (int) oscArgList.get(0);
        //yOffset is either 0 or 8
        int yOffset = (int) oscArgList.get(1);

        for(int i = 2; i < oscArgList.size(); i++){
            int ledState = (int) oscArgList.get(i);
            setLEDLevelCol(gridX, yOffset, ledState, i-2);
        }


    }

    public abstract void setLEDLevelCol(int gridX, int yOffset, int ledState, int yCounter);
}
