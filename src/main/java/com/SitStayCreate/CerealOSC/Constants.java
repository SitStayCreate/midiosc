package com.SitStayCreate.CerealOSC;

public class Constants {

    // ActionListeners
    // OSC Regexes
    // MonomeDevice Message strings - used when implementing a MonomeDevice
    public static final String FORMAT_STRING = "%s/grid/key";
    public static final String FORMAT_TYPE_TAG = "iii";
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
    public static final String MONOME = "monome ";

    // serialosc message strings
    public static final String LIST_STRING = "/serialosc/list";
    public static final String NOTIFY_STRING = "/serialosc/notify";
    public static final String DEVICE_STRING = "/serialosc/device";
    public static final String DEVICE_TYPE_TAG = "ssi";

    // Sys Message strings
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
