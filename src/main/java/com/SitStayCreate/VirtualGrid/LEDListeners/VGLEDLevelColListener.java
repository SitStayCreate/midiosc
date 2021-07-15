package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelColListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.awt.*;
import java.util.List;

public class VGLEDLevelColListener extends LEDLevelColListener {

    VGButton[][] buttonMatrix;
    private List<Color> colorValues;

    public VGLEDLevelColListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }

    @Override
    public void setLEDLevelCol(int gridX, int yOffset, int ledState, int yCounter) {

        //Virtual Grids are always 16x8
        if(yOffset == 8){
            return;
        }

        if(yCounter >= 8){
            return;
        }

        //Varibright supported
        if (ledState < 4){
            buttonMatrix[gridX][yOffset + yCounter].setBackground(colorValues.get(0));
        }  else if(ledState < 8){
            buttonMatrix[gridX][yOffset + yCounter].setBackground(colorValues.get(1));
        }  else if(ledState < 12) {
            buttonMatrix[gridX][yOffset + yCounter].setBackground(colorValues.get(2));
        } else {
            buttonMatrix[gridX][yOffset + yCounter].setBackground(colorValues.get(3));
        }
    }
}
