package com.SitStayCreate;

import com.SitStayCreate.GUI.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {

    private final static String TITLE = "CerealOSC";
    private final static int FRAME_WIDTH = 500;
    private final static int FRAME_HEIGHT = 360;

    public static void main(String[] args){
        Main gui = new Main();

        gui.go();
    }

    public void go(){
        JFrame frame = new JFrame(TITLE);
        JPanel jPanel = new JPanel();

        MainPanel mainPanel = new MainPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        jPanel.add(mainPanel);
        jPanel.setBackground(Color.DARK_GRAY);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.add(jPanel);
        frame.setVisible(true);
    }

}
