package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDSetListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.awt.*;
import java.util.List;

public class VGLEDSetListener extends LEDSetListener {

    private VGButton[][] buttonMatrix;
    List<Color> colorValues;

    public VGLEDSetListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }

    @Override
    public void setLEDState(int x, int y, int z) {
        //Virtual grids never have more than 8 rows, so y is never > 7.
        if(y >= 8){
            return;
        }

        if(z == 0){
            buttonMatrix[x][y].setBackground(colorValues.get(0));
        } else {
            buttonMatrix[x][y].setBackground(colorValues.get(3));
        }
    }
}
