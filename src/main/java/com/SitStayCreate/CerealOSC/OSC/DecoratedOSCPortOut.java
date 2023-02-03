package com.SitStayCreate.CerealOSC.OSC;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.transport.udp.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;

public class DecoratedOSCPortOut {

    private OSCPortOut oscPortOut;
    private int targetPort;
    private String hostName;

    //Most use cases will require this constructor
    public DecoratedOSCPortOut(int targetPort) throws IOException {
        this(InetAddress.getLocalHost(), targetPort);
    }

    //Potentially could use this with a singleboard computer setup
    public DecoratedOSCPortOut(InetAddress inetAddress, int targetPort) throws IOException {
        oscPortOut = new OSCPortOut(inetAddress, targetPort);
        this.targetPort = targetPort;
    }

    public int getTargetPort() {
        return targetPort;
    }

    //Call this when you need to change the targetPort (will reuse current host)
    public void setOscPortOut(int targetPort) throws IOException {
        setOscPortOut(hostName, targetPort);
        this.targetPort = targetPort;
    }

    //Call this when you need to change the hostname (will reuse current port number)
    public void setOscPortOut(String hostName) throws IOException {
        setOscPortOut(hostName, targetPort);
        this.hostName = hostName;
    }

    //TODO: update part of app that listens for /sys/host and /sys/port and /sys/info? (I think this is in GridController)
    //  previously the host was always set to localhost
    //Call this when you need to change the hostname and targetPort
    public void setOscPortOut(String hostName, int targetPort) throws IOException {
        oscPortOut.disconnect();
        oscPortOut.close();
        InetAddress inetAddress = InetAddress.getByName(hostName);
        oscPortOut = new OSCPortOut(inetAddress, targetPort);
        this.hostName = hostName;
        this.targetPort = targetPort;
    }

    //Call this when you are closing all resources
    public void close() throws IOException {
        oscPortOut.disconnect();
        oscPortOut.close();
    }

    //This class is needed to reduce coupling between the client and the decorated class
    public void send(OSCPacket packet) throws IOException, OSCSerializeException {
        oscPortOut.send(packet);
    }
}
