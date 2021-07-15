package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelMapListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.awt.*;
import java.util.List;

public class VGLEDLevelMapListener extends LEDLevelMapListener {

    VGButton[][] buttonMatrix;
    private List<Color> colorValues;

    public VGLEDLevelMapListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }


    @Override
    public void setLEDLevelMap(int xOffset, int yOffset, int ledState, int counter) {
        int xCounter = counter % 8; // increments to 7 then resets
        int yCounter = (int) Math.floor(counter / 8); // 0-7 = 0, 8-15 = 1, ...

        int gridX = xOffset + xCounter;
        int gridY = yOffset + yCounter;

        //Virtual grids are always 16x8
        if(gridY >= 8){
            return;
        }

        //Translate ledLevel to ledSet
        if(ledState < 4){
            buttonMatrix[gridX][gridY].setBackground(colorValues.get(0));
        } else if(ledState < 8){
            buttonMatrix[gridX][gridY].setBackground(colorValues.get(1));
        }  else if(ledState < 12){
            buttonMatrix[gridX][gridY].setBackground(colorValues.get(2));
        } else {
            buttonMatrix[gridX][gridY].setBackground(colorValues.get(3));
        }
    }
}
