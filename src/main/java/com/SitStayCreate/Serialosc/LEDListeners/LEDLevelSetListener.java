package com.SitStayCreate.Serialosc.LEDListeners;

import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageListener;

public abstract class LEDLevelSetListener implements OSCMessageListener {

    @Override
    public void acceptMessage(OSCMessageEvent event) {
        //Get data from Osc message
        int gridX = (int) event.getMessage().getArguments().get(0);
        int gridY = (int) event.getMessage().getArguments().get(1);
        int gridZ = (int) event.getMessage().getArguments().get(2);

        setLEDLevelState(gridX, gridY, gridZ);
    }

    public abstract void setLEDLevelState(int x, int y, int z);
}
