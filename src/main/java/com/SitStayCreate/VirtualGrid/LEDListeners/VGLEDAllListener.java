package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDAllListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.awt.*;
import java.util.List;

public class VGLEDAllListener extends LEDAllListener {

    VGButton[][] buttonMatrix;
    private List<Color> colorValues;

    public VGLEDAllListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }

    @Override
    public void setLedAllState(int state) {
        for(VGButton[] buttonArr : buttonMatrix){
            for(VGButton button : buttonArr){
                if(state == 0){
                    button.setBackground(colorValues.get(0));
                } else {
                    button.setBackground(colorValues.get(3));
                }
            }
        }
    }
}
