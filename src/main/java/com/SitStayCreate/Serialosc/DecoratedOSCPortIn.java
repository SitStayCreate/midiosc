package com.SitStayCreate.Serialosc;

import com.illposed.osc.OSCPacketDispatcher;
import com.illposed.osc.transport.udp.OSCPortIn;

import java.io.IOException;

public class DecoratedOSCPortIn {

    private int portIn;
    private OSCPortIn oscPortIn;

    public DecoratedOSCPortIn(int portIn) throws IOException {
        this(new OSCPortIn(portIn), portIn);
    }

    public DecoratedOSCPortIn(OSCPortIn oscPortIn, int portIn) throws IOException {
        this.oscPortIn = oscPortIn;
        this.portIn = portIn;
    }

    public int getPortIn() {
        return portIn;
    }

    //Call this when you need a new port
    public void setPortIn(int portIn) throws IOException {
        oscPortIn.disconnect();
        oscPortIn.close();
        oscPortIn = new OSCPortIn(portIn);
        this.portIn = portIn;
    }

    //This method is needed to reduce coupling between the client and the decorated class
    public OSCPacketDispatcher getDispatcher(){
        return oscPortIn.getDispatcher();
    }

    public OSCPortIn getOscPortIn() {
        return oscPortIn;
    }

    //Call this when closing all resources
    public void close() throws IOException {
        oscPortIn.stopListening();
        oscPortIn.disconnect();
        oscPortIn.close();
        portIn = 0;
    }

    public void startListening(){
        oscPortIn.startListening();
    }

    public void stopListening(){
        oscPortIn.stopListening();
    }
}
