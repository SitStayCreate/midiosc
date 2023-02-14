package com.SitStayCreate.CerealOSC.MonomeApp;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

//This class is for the target/destination monomeApp (the one you want to send messages to).
public class MonomeApp {

    private int portNumber;
    private String hostName;

    public MonomeApp(int portNumber) {
        this("localhost", portNumber);
    }

    public MonomeApp(String hostName, int portNumber){
        //Convert all cases of localhost to the localhost IP string
        if(hostName.toLowerCase().equals("localhost")){
            hostName = "127.0.0.1";
        }
        setHostName(hostName);
        setPortNumber(portNumber);
    }

    public String getHostName() {
        return hostName;
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName(hostName);
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    //Everything below this point generated in IntelliJ IDEA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonomeApp monomeApp = (MonomeApp) o;
        return getPortNumber() == monomeApp.getPortNumber() &&
                getHostName().equals(monomeApp.getHostName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPortNumber(), getHostName());
    }

    @Override
    public String toString() {
        return "MonomeApp{" +
                "portNumber=" + portNumber +
                ", hostName='" + hostName + '\'' +
                '}';
    }
}
