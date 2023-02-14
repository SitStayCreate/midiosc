package com.SitStayCreate.CerealOSC.MonomeDevice;

public class Dimensions {
    private int height;
    private int width;
    private boolean inverted;

    public Dimensions(int height, int width, boolean inverted){
        setHeight(height);
        setWidth(width);
        setInverted(inverted);
    }

    public int getArea(){
        return width * height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
