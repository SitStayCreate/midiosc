package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelAllListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.awt.*;
import java.util.List;

public class VGLEDLevelAllListener extends LEDLevelAllListener {

    VGButton[][] buttonMatrix;
    private List<Color> colorValues;

    public VGLEDLevelAllListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }


    @Override
    public void setLEDLevelAll(int s) {

        for(VGButton[] buttonArr : buttonMatrix){
            for(VGButton button : buttonArr){
                if(s < 4){
                    button.setBackground(colorValues.get(0));
                } else if(s < 8){
                    button.setBackground(colorValues.get(1));
                }  else if(s < 12){
                    button.setBackground(colorValues.get(2));
                } else {
                    button.setBackground(colorValues.get(3));
                }
            }
        }
    }
}
