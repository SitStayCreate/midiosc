package com.SitStayCreate.Serialosc.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

public abstract class LEDColListener implements OSCMessageListener {

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //OSC stuff
        //get args
        int gridX = (int) event.getMessage().getArguments().get(0);
        //This is the row you're starting on
        int yOffset = (int) event.getMessage().getArguments().get(1);

        for(int i = 2; i < event.getMessage().getArguments().size(); i++) {
            int bitmask = (int) event.getMessage().getArguments().get(i);
            int yCounter = (i-2) * 8;
            //There could potentially be a second bitmask for 8x16 grids
            //int bitmask2 = (int) oscMessage.getArguments().get(3);
            String binaryString = Integer.toBinaryString(bitmask);
            setLEDColState(binaryString, gridX, yOffset, yCounter);
        }
    }

    public abstract void setLEDColState(String binaryString, int x, int yOffset, int yCounter);
}
