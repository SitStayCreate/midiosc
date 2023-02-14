package com.SitStayCreate.CerealOSC.MonomeDevice;

import com.SitStayCreate.CerealOSC.MonomeApp.MonomeApp;
import com.SitStayCreate.CerealOSC.OSC.DecoratedOSCPortIn;
import com.SitStayCreate.CerealOSC.OSC.DecoratedOSCPortOut;
import com.SitStayCreate.CerealOSC.RequestServer.RequestServer;
import com.SitStayCreate.CerealOSC.SysListeners.SysHostListener;
import com.SitStayCreate.CerealOSC.SysListeners.SysInfoListener;
import com.SitStayCreate.CerealOSC.SysListeners.SysPortListener;
import com.SitStayCreate.CerealOSC.SysListeners.SysPrefixListener;
import com.SitStayCreate.Constants;
import com.illposed.osc.*;
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector;

import java.io.IOException;

public abstract class GridController extends MonomeController {

    protected Dimensions dimensions;

    public GridController(MonomeApp monomeApp,
                          DecoratedOSCPortIn decoratedOSCPortIn,
                          DecoratedOSCPortOut decoratedOSCPortOut,
                          Dimensions dimensions,
                          RequestServer requestServer) {
        super(monomeApp, decoratedOSCPortIn, decoratedOSCPortOut, requestServer);
        setDimensions(dimensions);
        addSysListeners();
        getDecoratedOSCPortIn().startListening();
    }

    //TODO: Should this be in the parent?
    public void send(OSCPacket packet) throws IOException, OSCSerializeException {
        decoratedOSCPortOut.send(packet);
    }

    private void setDimensions(Dimensions dimensions){
        this.dimensions = dimensions;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    //TODO: Should this be in the parent?
    @Override
    public void addSysListeners(){
        //Listens for /sys/prefix messages
        MessageSelector sysPrefixSelector = new OSCPatternAddressMessageSelector(Constants.SYS_PREFIX_MESSAGE);
        SysPrefixListener sysPrefixMessageListener = new SysPrefixListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysPrefixSelector, sysPrefixMessageListener);

        //Listens for /sys/port messages
        MessageSelector sysPortSelector = new OSCPatternAddressMessageSelector(Constants.SYS_PORT_MESSAGE);
        SysPortListener sysPortMessageListener = new SysPortListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysPortSelector, sysPortMessageListener);

        //Listens for /sys/info messages
        MessageSelector sysInfoSelector = new OSCPatternAddressMessageSelector(Constants.SYS_INFO_MESSAGE);
        SysInfoListener sysInfoListener = new SysInfoListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysInfoSelector, sysInfoListener);

        //Listens for /sys/host messages
        MessageSelector sysHostSelector = new OSCPatternAddressMessageSelector(Constants.SYS_HOST_MESSAGE);
        SysHostListener sysHostListener = new SysHostListener(this);
        decoratedOSCPortIn.getDispatcher().addListener(sysHostSelector, sysHostListener);
    }

    public abstract void addLEDListeners();

    @Override
    public void close(){
        try {
            decoratedOSCPortIn.close();
            decoratedOSCPortOut.close();
            requestServer.removeMonomeController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
