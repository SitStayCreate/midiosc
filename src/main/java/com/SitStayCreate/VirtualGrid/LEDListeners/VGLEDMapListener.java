package com.SitStayCreate.VirtualGrid.LEDListeners;

import com.SitStayCreate.Serialosc.LEDListeners.LEDMapListener;
import com.SitStayCreate.VirtualGrid.VGButton;

import java.util.List;
import java.awt.*;

public class VGLEDMapListener extends LEDMapListener {
    VGButton[][] buttonMatrix;
    private List<Color> colorValues;

    public VGLEDMapListener(VGButton[][] buttonMatrix, List<Color> colorValues){
        this.buttonMatrix = buttonMatrix;
        this.colorValues = colorValues;
    }

    @Override
    public void setLEDMap(String binaryString, int xOffset, int yOffset, int yCounter){

        //Virtual Grids will always be 16x8
        if(yOffset == 8){
            return;
        }

        int xCounter = 0;

        //Add leading 0s if fewer than 8 bits
        for(int j = 7; j >= binaryString.length(); j--){
            buttonMatrix[xOffset + xCounter][yOffset + yCounter].setBackground(colorValues.get(0));
            xCounter++;
        }

        byte[] bytes = binaryString.getBytes();

        //add data from message
        for(int j = 0; j < binaryString.length(); j++){

            byte b = bytes[j];
            int gridZ = 0;

            if ((b & 1) != 0){
                gridZ = 1;
            }

            if(gridZ == 0){
                buttonMatrix[xOffset + xCounter][yOffset + yCounter].setBackground(colorValues.get(0));
            } else {
                buttonMatrix[xOffset + xCounter][yOffset + yCounter].setBackground(colorValues.get(3));
            }

            xCounter++;
        }
    }
}
