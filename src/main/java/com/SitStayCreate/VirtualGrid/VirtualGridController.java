package com.SitStayCreate.VirtualGrid;

import com.SitStayCreate.Serialosc.*;
import com.SitStayCreate.VirtualGrid.LEDListeners.*;
import com.illposed.osc.*;
import com.illposed.osc.messageselector.JavaRegexAddressMessageSelector;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirtualGridController extends GridController {
    private static final List<Color> LEDCOLORS = new ArrayList<>(4);
    private VGButton[][] buttonMatrix;
    private VGridFrame vGridFrame;

    //Use this constructor if no Apps have registered with the RequestServer - Max/MSP represents an unknown port as 0
    //so I'm following this convention.
    public VirtualGridController(int portInNumber, VGridFrame vGridFrame) throws IOException {
        this(new MonomeApp(0), portInNumber);
        this.vGridFrame = vGridFrame;
    }

    //Use this constructor if no Apps have registered with the RequestServer - Max/MSP represents an unknown port as 0
    //so I'm following this convention.
    public VirtualGridController(int portInNumber) throws IOException {
        this(new MonomeApp(0), portInNumber);
    }

    //Use this constructor if MonomeApp has registered with the RequestServer
    public VirtualGridController(MonomeApp monomeApp,
                                 int portInNumber) throws IOException {
        super(monomeApp,
            new DecoratedOSCPortIn(portInNumber),
            new DecoratedOSCPortOut(monomeApp.getInetAddress(),
            monomeApp.getPortNumber()),
            new Dimensions(8, 16, false));
        LEDCOLORS.add(Color.DARK_GRAY);
        LEDCOLORS.add(Color.decode("#006666"));
        LEDCOLORS.add(Color.decode("#00b3b3"));
        LEDCOLORS.add(Color.decode("#00ffff"));

        buttonMatrix = new VGButton[dimensions.getWidth()][dimensions.getHeight()];

        for (int j = 0; j < dimensions.getHeight(); j++){
            for (int i = 0; i < dimensions.getWidth(); i++){
                    buttonMatrix[i][j] = new VGButton();
                    buttonMatrix[i][j].setPreferredSize(new Dimension(60, 60));
                    final int x = i;
                    final int y = j;
                    buttonMatrix[i][j].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            //Do nothing
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            String addressString = String.format("%s/grid/key", getPrefix());
                            List<Integer> oscArgs = new ArrayList<>();
                            oscArgs.add(x);
                            oscArgs.add(y);
                            oscArgs.add(1);

                            OSCMessageInfo oscMessageInfo = new OSCMessageInfo("iii");
                            OSCMessage oscMessage = new OSCMessage(addressString, oscArgs, oscMessageInfo);
                            try {
                                System.out.println(getPrefix());
                                System.out.println(oscArgs);
                                send(oscMessage);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (OSCSerializeException ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            String addressString = String.format("%s/grid/key", getPrefix());
                            List<Integer> oscArgs = new ArrayList<>();
                            oscArgs.add(x);
                            oscArgs.add(y);
                            oscArgs.add(0);

                            OSCMessageInfo oscMessageInfo = new OSCMessageInfo("iii");
                            OSCMessage oscMessage = new OSCMessage(addressString, oscArgs, oscMessageInfo);
                            try {
                                System.out.println(oscArgs);
                                send(oscMessage);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (OSCSerializeException ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            //Do nothing
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            //Do nothing
                        }
                    });
            }
        }

        addLEDListeners();
    }

    public VGButton[][] getButtonMatrix(){
        return buttonMatrix;
    }

    @Override
    public void addLEDListeners() {
        String ledSetSelectorRegex = "/.*/grid/led/set";
        JavaRegexAddressMessageSelector ledSetMessageSelector = new JavaRegexAddressMessageSelector(ledSetSelectorRegex);
        VGLEDSetListener vgledSetListener = new VGLEDSetListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledSetMessageSelector, vgledSetListener);

        String ledAllSelectorRegex = "/.*/grid/led/all";
        JavaRegexAddressMessageSelector ledAllMessageSelector = new JavaRegexAddressMessageSelector(ledAllSelectorRegex);
        VGLEDAllListener vgledAllListener = new VGLEDAllListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledAllMessageSelector, vgledAllListener);

        String ledMapSelectorRegex = "/.*/grid/led/map";
        JavaRegexAddressMessageSelector ledMapMessageSelector = new JavaRegexAddressMessageSelector(ledMapSelectorRegex);
        VGLEDMapListener vgledMapListener = new VGLEDMapListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledMapMessageSelector, vgledMapListener);

        String ledRowSelectorRegex = "/.*/grid/led/row";
        JavaRegexAddressMessageSelector ledRowMessageSelector = new JavaRegexAddressMessageSelector(ledRowSelectorRegex);
        VGLEDRowListener vgledRowListener = new VGLEDRowListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledRowMessageSelector, vgledRowListener);

        String ledColSelectorRegex = "/.*/grid/led/col";
        JavaRegexAddressMessageSelector ledColMessageSelector = new JavaRegexAddressMessageSelector(ledColSelectorRegex);
        VGLEDColListener vgledColListener = new VGLEDColListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledColMessageSelector, vgledColListener);

        String ledLevelSetSelectorRegex = "/.*/grid/led/level/set";
        JavaRegexAddressMessageSelector ledLevelSetMessageSelector = new JavaRegexAddressMessageSelector(ledLevelSetSelectorRegex);
        VGLEDLevelSetListener vgledLevelSetListener = new VGLEDLevelSetListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelSetMessageSelector, vgledLevelSetListener);

        String ledLevelAllSelectorRegex = "/.*/grid/led/level/all";
        JavaRegexAddressMessageSelector ledLevelAllMessageSelector = new JavaRegexAddressMessageSelector(ledLevelAllSelectorRegex);
        VGLEDLevelAllListener vgledLevelAllListener = new VGLEDLevelAllListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelAllMessageSelector, vgledLevelAllListener);

        String ledLevelMapSelectorRegex = "/.*/grid/led/level/map";
        JavaRegexAddressMessageSelector ledLevelMapMessageSelector = new JavaRegexAddressMessageSelector(ledLevelMapSelectorRegex);
        VGLEDLevelMapListener vgledLevelMapListener = new VGLEDLevelMapListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelMapMessageSelector, vgledLevelMapListener);

        String ledLevelRowSelectorRegex = "/.*/grid/led/level/row";
        JavaRegexAddressMessageSelector ledLevelRowMessageSelector = new JavaRegexAddressMessageSelector(ledLevelRowSelectorRegex);
        VGLEDLevelRowListener vgledLevelRowListener = new VGLEDLevelRowListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelRowMessageSelector, vgledLevelRowListener);

        String ledLevelColSelectorRegex = "/.*/grid/led/level/col";
        JavaRegexAddressMessageSelector ledLevelColMessageSelector = new JavaRegexAddressMessageSelector(ledLevelColSelectorRegex);
        VGLEDLevelColListener vgledLevelColListener = new VGLEDLevelColListener(buttonMatrix, LEDCOLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelColMessageSelector, vgledLevelColListener);
    }

    @Override
    public void close(){
        //TODO: stuff needed to close this device and also close the GUI window
        try {
            decoratedOSCPortIn.close();
            decoratedOSCPortOut.close();
            vGridFrame.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
