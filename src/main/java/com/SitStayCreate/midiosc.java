package com.SitStayCreate;

import javax.swing.*;
import java.awt.*;


public class midiosc {

    public static void main(String[] args) {

        midiosc gui = new midiosc();

        gui.go();
    }

    public void go(){
        JFrame frame = new JFrame("midiosc-1.0");
        JPanel jPanel = new JPanel();
        OSCDeviceJPanel oscDeviceJPanel = new OSCDeviceJPanel();
        oscDeviceJPanel.setBackground(Color.DARK_GRAY);
        jPanel.add(oscDeviceJPanel);
        jPanel.setBackground(Color.DARK_GRAY);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 360);
        frame.add(jPanel);
        frame.setVisible(true);
    }

}