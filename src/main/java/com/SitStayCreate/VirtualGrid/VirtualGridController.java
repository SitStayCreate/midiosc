package com.SitStayCreate.VirtualGrid;

import com.SitStayCreate.Serialosc.*;
import com.SitStayCreate.VirtualGrid.LEDListeners.*;
import com.illposed.osc.*;
import com.illposed.osc.messageselector.JavaRegexAddressMessageSelector;
import com.SitStayCreate.Constants;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirtualGridController extends GridController {
    private static final List<Color> LED_COLORS = new ArrayList<>(Constants.LED_COLOR_CAPACITY);
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
            new Dimensions(Constants.HEIGHT, Constants.WIDTH, false));
        LED_COLORS.add(Color.DARK_GRAY);
        LED_COLORS.add(Color.decode(Constants.COLOR_LEVEL_TWO));
        LED_COLORS.add(Color.decode(Constants.COLOR_LEVEL_THREE));
        LED_COLORS.add(Color.decode(Constants.COLOR_LEVEL_FOUR));

        buttonMatrix = new VGButton[dimensions.getWidth()][dimensions.getHeight()];

        for (int j = 0; j < dimensions.getHeight(); j++){
            for (int i = 0; i < dimensions.getWidth(); i++){
                    buttonMatrix[i][j] = new VGButton();
                    buttonMatrix[i][j].setPreferredSize(new Dimension(Constants.BUTTON_SIZE, Constants.BUTTON_SIZE));
                    final int x = i;
                    final int y = j;
                    buttonMatrix[i][j].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            //Do nothing
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            String addressString = String.format(Constants.FORMAT_STRING, getPrefix());
                            List<Integer> oscArgs = new ArrayList<>();
                            oscArgs.add(x);
                            oscArgs.add(y);
                            oscArgs.add(1);

                            OSCMessageInfo oscMessageInfo = new OSCMessageInfo(Constants.FORMAT_TYPE_TAG);
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
                            String addressString = String.format(Constants.FORMAT_STRING, getPrefix());
                            List<Integer> oscArgs = new ArrayList<>();
                            oscArgs.add(x);
                            oscArgs.add(y);
                            oscArgs.add(0);

                            OSCMessageInfo oscMessageInfo = new OSCMessageInfo(Constants.FORMAT_TYPE_TAG);
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
        String ledSetSelectorRegex = Constants.LED_SET_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledSetMessageSelector = new JavaRegexAddressMessageSelector(ledSetSelectorRegex);
        VGLEDSetListener vgledSetListener = new VGLEDSetListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getOscPortIn().getDispatcher().addListener(ledSetMessageSelector, vgledSetListener);

        String ledAllSelectorRegex = Constants.LED_ALL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledAllMessageSelector = new JavaRegexAddressMessageSelector(ledAllSelectorRegex);
        VGLEDAllListener vgledAllListener = new VGLEDAllListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledAllMessageSelector, vgledAllListener);

        String ledMapSelectorRegex = Constants.LED_MAP_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledMapMessageSelector = new JavaRegexAddressMessageSelector(ledMapSelectorRegex);
        VGLEDMapListener vgledMapListener = new VGLEDMapListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledMapMessageSelector, vgledMapListener);

        String ledRowSelectorRegex = Constants.LED_ROW_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledRowMessageSelector = new JavaRegexAddressMessageSelector(ledRowSelectorRegex);
        VGLEDRowListener vgledRowListener = new VGLEDRowListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledRowMessageSelector, vgledRowListener);

        String ledColSelectorRegex = Constants.LED_COL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledColMessageSelector = new JavaRegexAddressMessageSelector(ledColSelectorRegex);
        VGLEDColListener vgledColListener = new VGLEDColListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledColMessageSelector, vgledColListener);

        String ledLevelSetSelectorRegex = Constants.LED_LEVEL_SET_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelSetMessageSelector = new JavaRegexAddressMessageSelector(ledLevelSetSelectorRegex);
        VGLEDLevelSetListener vgledLevelSetListener = new VGLEDLevelSetListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelSetMessageSelector, vgledLevelSetListener);

        String ledLevelAllSelectorRegex = Constants.LED_LEVEL_ALL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelAllMessageSelector = new JavaRegexAddressMessageSelector(ledLevelAllSelectorRegex);
        VGLEDLevelAllListener vgledLevelAllListener = new VGLEDLevelAllListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelAllMessageSelector, vgledLevelAllListener);

        String ledLevelMapSelectorRegex = Constants.LED_LEVEL_MAP_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelMapMessageSelector = new JavaRegexAddressMessageSelector(ledLevelMapSelectorRegex);
        VGLEDLevelMapListener vgledLevelMapListener = new VGLEDLevelMapListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelMapMessageSelector, vgledLevelMapListener);

        String ledLevelRowSelectorRegex = Constants.LED_LEVEL_ROW_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelRowMessageSelector = new JavaRegexAddressMessageSelector(ledLevelRowSelectorRegex);
        VGLEDLevelRowListener vgledLevelRowListener = new VGLEDLevelRowListener(buttonMatrix, LED_COLORS);
        decoratedOSCPortIn.getDispatcher().addListener(ledLevelRowMessageSelector, vgledLevelRowListener);

        String ledLevelColSelectorRegex = Constants.LED_LEVEL_COL_SELECTOR_REGEX;
        JavaRegexAddressMessageSelector ledLevelColMessageSelector = new JavaRegexAddressMessageSelector(ledLevelColSelectorRegex);
        VGLEDLevelColListener vgledLevelColListener = new VGLEDLevelColListener(buttonMatrix, LED_COLORS);
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
