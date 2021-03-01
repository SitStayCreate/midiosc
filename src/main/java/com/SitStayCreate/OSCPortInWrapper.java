package com.SitStayCreate;

import com.illposed.osc.transport.udp.OSCPortIn;

public class OSCPortInWrapper {
    private OSCPortIn oscPortIn;
    private int portInNumber;

    public OSCPortInWrapper(){

    }

    public OSCPortInWrapper(OSCPortIn oscPortIn, int portInNumber){
        this.oscPortIn = oscPortIn;
        this.portInNumber = portInNumber;
    }

    public void setOscPortIn(OSCPortIn oscPortIn) {
        this.oscPortIn = oscPortIn;
    }

    public void setPortInNumber(int portInNumber) {
        this.portInNumber = portInNumber;
    }

    public OSCPortIn getOscPortIn() {
        return oscPortIn;
    }

    public int getPortInNumber() {
        return portInNumber;
    }
}
