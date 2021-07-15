package com.SitStayCreate.Serialosc;

import com.SitStayCreate.Serialosc.SysListeners.SysHostListener;
import com.SitStayCreate.Serialosc.SysListeners.SysInfoListener;
import com.SitStayCreate.Serialosc.SysListeners.SysPortListener;
import com.SitStayCreate.Serialosc.SysListeners.SysPrefixListener;
import com.illposed.osc.*;
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector;

import java.io.IOException;

public abstract class GridController extends MonomeController {

    protected Dimensions dimensions;

    public GridController(MonomeApp monomeApp,
                          DecoratedOSCPortIn decoratedOSCPortIn,
                          DecoratedOSCPortOut decoratedOSCPortOut,
                          Dimensions dimensions) {
        super(monomeApp, decoratedOSCPortIn, decoratedOSCPortOut);
        this.dimensions = dimensions;
        addSysListeners();
        getDecoratedOSCPortIn().startListening();
    }

    //TODO: Should this be in the parent?
    public void send(OSCPacket packet) throws IOException, OSCSerializeException {
        decoratedOSCPortOut.send(packet);
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    //TODO: Should this be in the parent?
    @Override
    public void addSysListeners(){
        //Listens for /sys/prefix messages
        String sysPrefixString = "/sys/prefix";
        MessageSelector sysPrefixSelector = new OSCPatternAddressMessageSelector(sysPrefixString);
        SysPrefixListener sysPrefixMessageListener = new SysPrefixListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysPrefixSelector, sysPrefixMessageListener);

        //Listens for /sys/port messages
        String sysPortString = "/sys/port";
        MessageSelector sysPortSelector = new OSCPatternAddressMessageSelector(sysPortString);
        SysPortListener sysPortMessageListener = new SysPortListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysPortSelector, sysPortMessageListener);

        //Listens for /sys/info messages
        String sysInfoString = "/sys/info";
        MessageSelector sysInfoSelector = new OSCPatternAddressMessageSelector(sysInfoString);
        SysInfoListener sysInfoListener = new SysInfoListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysInfoSelector, sysInfoListener);

        //Listens for /sys/host messages
        String sysHostString = "/sys/host";
        MessageSelector sysHostSelector = new OSCPatternAddressMessageSelector(sysHostString);
        SysHostListener sysHostListener = new SysHostListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysHostSelector, sysHostListener);
    }

    public abstract void addLEDListeners();
}
