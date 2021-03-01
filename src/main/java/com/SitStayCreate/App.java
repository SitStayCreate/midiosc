package com.SitStayCreate;


import java.net.InetAddress;

//This class is for notify messages from the request server
public class App {

    private InetAddress inetAddress;
    private int portNumber;

    public App(){

    }

    public App(InetAddress inetAddress, int portNumber){
        this.inetAddress = inetAddress;
        this.portNumber = portNumber;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
