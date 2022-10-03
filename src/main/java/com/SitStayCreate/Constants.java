package com.SitStayCreate;

public class Constants {

    // GUI
    // MainPanel
    public final static int FONT_SIZE = 18;

    // MidiPanel
    public final static String MIDI_IN_LABEL = "Midi in: ";
    public final static String MIDI_OUT_LABEL = "Midi out: ";
    public final static String DIMENS_LABEL = "Dimens: ";
    public final static String RADIO_BUTTON1_LABEL = "64";
    public final static String RADIO_BUTTON2_LABEL = "128";
    public final static String INVERT_LABEL = "Invert Y-Axis";
    public final static String CHANNEL_LABEL = "Channel: ";
    public final static String ELIPSES = "...";
    public final static String REGEX_SPLIT = ":";

    // GridPanel
    public final static String PORT_IN_LABEL = "Port in: ";
    public final static String DEFAULT_PORT_NUMBER = "8080";
    public final static String TYPE_LABEL = "Type: ";
    public final static String TYPE_RB1_LABEL = "Midi";
    public final static String TYPE_RB2_LABEL = "VGrid";
    public final static String CREATE_BUTTON_LABEL = "Create";
    public final static int CREATE_BUTTON_WIDTH = 120;
    public final static int CREATE_BUTTON_HEIGHT = 40;

    // DevicesTable
    public final static String DROP_LABEL = "Drop";
    public final static String ID_COLUMN_LABEL = "ID";
    public final static String SIZE_COLUMN_LABEL = " Size ";
    public final static String PORT_IN_COLUMN_LABEL = "Port In";
    public final static String INVERTED_COLUMN_LABEL = "Inverted";
    public final static String CH_LABEL = "Ch";
    public final static String DELETE_LABEL = "X";
    public final static String DEVICE_DIMS_LABEL = "x";
    public final static int VIEWPORT_WIDTH = 300;
    public final static int VIEWPORT_HEIGHT = 50;

    // DTPane
    public final static int DTPANE_WIDTH = 300;
    public final static int DTPANE_HEIGHT = 100;

    // ActionListeners

    // SBActionListener
    public final static String ERROR_LABEL = "Unavailable port";
    public final static int DEFAULT_PORT = 10000;

    // OSC Regexes
    // OSCTranslator and VirtualGridController
    public static final String FORMAT_STRING = "%s/grid/key";
    public static final String FORMAT_TYPE_TAG = "iii";

    // MidiGridAdapter and VirtualGridController
    public static final String LED_SET_SELECTOR_REGEX = "/.*/grid/led/set";
    public static final String LED_ALL_SELECTOR_REGEX = "/.*/grid/led/all";
    public static final String LED_MAP_SELECTOR_REGEX = "/.*/grid/led/map";
    public static final String LED_ROW_SELECTOR_REGEX = "/.*/grid/led/row";
    public static final String LED_COL_SELECTOR_REGEX = "/.*/grid/led/col";
    public static final String LED_LEVEL_SET_SELECTOR_REGEX = "/.*/grid/led/level/set";
    public static final String LED_LEVEL_ALL_SELECTOR_REGEX = "/.*/grid/led/level/all";
    public static final String LED_LEVEL_MAP_SELECTOR_REGEX = "/.*/grid/led/level/map";
    public static final String LED_LEVEL_ROW_SELECTOR_REGEX = "/.*/grid/led/level/row";
    public static final String LED_LEVEL_COL_SELECTOR_REGEX = "/.*/grid/led/level/col";

    // RequestServer
    public static final int PORT_NUMBER = 12002;
    public static final String THEY_WHO_SHALL_NOT_BE_NAMED = "monome ";

    // VirtualGridController
    public final static int LED_COLOR_CAPACITY = 4;
    public final static int HEIGHT = 8;
    public final static int WIDTH = 16;
    public final static String COLOR_LEVEL_TWO = "#006666";
    public final static String COLOR_LEVEL_THREE = "#00b3b3";
    public final static String COLOR_LEVEL_FOUR = "#00ffff";
    public final static int BUTTON_SIZE = 60;

    // serialosc messages
    // RequestServer
    public static final String LIST_STRING = "/serialosc/list";
    public static final String NOTIFY_STRING = "/serialosc/notify";
    public static final String DEVICE_STRING = "/serialosc/device";
    public static final String DEVICE_TYPE_TAG = "ssi";

    // Sys Messages
    // SysInfo Listener and GridController
    public static final String SYS_PORT_MESSAGE = "/sys/port";
    public static final String SYS_PORT_TYPE_TAG = "i";
    public static final String SYS_HOST_MESSAGE = "/sys/host";
    public static final String SYS_HOST_TYPE_TAG = "s";
    public static final String SYS_ID_MESSAGE = "/sys/id";
    public static final String SYS_ID_TYPE_TAG = "s";
    public static final String SYS_PREFIX_MESSAGE = "/sys/prefix";
    public static final String SYS_PREFIX_TYPE_TAG = "s";
    public static final String SYS_ROTATION_MESSAGE = "/sys/rotation";
    public static final String SYS_ROTATION_TYPE_TAG = "i";
    public static final String SYS_SIZE_MESSAGE = "/sys/size";
    public static final String SYS_SIZE_TYPE_TAG = "ii";
    public static final String SYS_INFO_MESSAGE = "/sys/info";
}
