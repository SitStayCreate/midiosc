package com.SitStayCreate;

import com.SitStayCreate.GUI.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){
        Main gui = new Main();

//        gui.go();
        gui.go2();
    }

    public void go(){
        JFrame frame = new JFrame("midiosc-1.1.0");
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

    public void go2(){
        JFrame frame = new JFrame("midiosc-1.1.0");
        JPanel jPanel = new JPanel();

        MainPanel mainPanel = new MainPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        jPanel.add(mainPanel);
        jPanel.setBackground(Color.DARK_GRAY);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 360);
        frame.add(jPanel);
        frame.setVisible(true);
    }

}
