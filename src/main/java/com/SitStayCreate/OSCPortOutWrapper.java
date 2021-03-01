package com.SitStayCreate;

import com.illposed.osc.transport.udp.OSCPortOut;

import java.io.IOException;

public class OSCPortOutWrapper {
    private OSCPortOut oscPortOut;
    private int portOutNumber;

    public OSCPortOutWrapper(OSCPortOut oscPortOut, int portOutNumber){
        this.oscPortOut = oscPortOut;
        this.portOutNumber = portOutNumber;
    }

    public void setOscPortOut(OSCPortOut oscPortOut) {
        try {
            this.oscPortOut.close();
        } catch (IOException ioex){
            ioex.printStackTrace();
        }
        this.oscPortOut = oscPortOut;
    }

    public void setPortOutNumber(int portOutNumber) {
        this.portOutNumber = portOutNumber;
    }

    public OSCPortOut getOscPortOut() {
        return oscPortOut;
    }

    public int getPortOutNumber() {
        return portOutNumber;
    }
}
