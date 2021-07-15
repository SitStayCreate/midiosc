package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDLevelSetListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.awt.*;
import java.util.List;

public class VGLEDLevelSetListener extends LEDLevelSetListener {
    VGButton[][] buttonMatrix;
    private List<Color> colorValues;

    public VGLEDLevelSetListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }

    @Override
    public void setLEDLevelState(int x, int y, int z) {

        if(y >= 8){
            return;
        }

        //Translate ledLevel to ledSet
        if(z < 4){
            buttonMatrix[x][y].setBackground(colorValues.get(0));
        } else if(z < 8){
            buttonMatrix[x][y].setBackground(colorValues.get(1));
        }  else if(z < 12){
            buttonMatrix[x][y].setBackground(colorValues.get(2));
        } else {
            buttonMatrix[x][y].setBackground(colorValues.get(3));
        }
    }
}
