package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelRowListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.awt.*;
import java.util.List;

public class VGLEDLevelRowListener extends LEDLevelRowListener {

    VGButton[][] buttonMatrix;
    private List<Color> colorValues;

    public VGLEDLevelRowListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }


    @Override
    public void setLEDLevelRow(int xOffset, int gridY, int xCounter, int ledState) {

        //Virtual grids never have more than 8 rows
        if(gridY >= 8){
            return;
        }

        //Varibright supported
        if (ledState < 4) {
            buttonMatrix[xOffset + (xCounter)][gridY].setBackground(colorValues.get(0));
        } else if (ledState < 8) {
            buttonMatrix[xOffset + (xCounter)][gridY].setBackground(colorValues.get(1));
        } else if (ledState < 12) {
            buttonMatrix[xOffset + (xCounter)][gridY].setBackground(colorValues.get(2));
        } else {
            buttonMatrix[xOffset + (xCounter)][gridY].setBackground(colorValues.get(3));
        }
    }
}
