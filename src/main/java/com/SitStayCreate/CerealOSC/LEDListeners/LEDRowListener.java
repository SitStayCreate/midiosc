package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDRowListener implements OSCMessageListener {
    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //OSC stuff
        //get args
        List args = event.getMessage().getArguments();
        int xOffset = (int) args.get(0);
        //This is the row you're starting on
        int gridY = (int) args.get(1);
        //For the rest of the arguments (1 or 2 more bitmasks)
        for(int i = 2; i < args.size(); i++) {
            int bitmask = (int) args.get(i);
            int xCounter = (i-2) * 8;
            String binaryString = Integer.toBinaryString(bitmask);
            setLEDRowState(binaryString, xOffset, xCounter, gridY);
        }
    }

    public abstract void setLEDRowState(String binaryString, int xOffset, int xCounter, int y);
}
