package com.SitStayCreate.CerealOSC.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

import java.util.List;

public abstract class LEDLevelMapListener implements OSCMessageListener {

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //This will always be 0 with a 64 grid - this represents the quad (group of 8x8)
        int xOffset = (int) event.getMessage().getArguments().get(0);
        //This is the row you're starting on
        int yOffset = (int) event.getMessage().getArguments().get(1);

        List oscList = event.getMessage().getArguments();

        //yOffset should never be greater than 0 on 8x8 and 8x16 grids
        if(yOffset > 0){
            return;
        }

        //When do these increment?
        //Because of xOffset. when it's 0 this is fine but when it's 8 it's not.
        for(int i = 2; i < oscList.size(); i++){
            int ledState = (int) oscList.get(i);
            int counter = (i-2);
            setLEDLevelMap(xOffset, yOffset, ledState, counter);
        }
    }

    public abstract void setLEDLevelMap(int xOffset, int yOffset, int ledState, int counter);
}
