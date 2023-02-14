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
    }

    public int getTargetPort() {
        return targetPort;
    }

    // Set the target port, this will only be called by this class
    private void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    // Set the hostname, this will only be called by this class
    private void setHostName(String hostName) {
        this.hostName = hostName;
    }

    //Call this when you need to change the targetPort (will reuse current host)
    public void setOscPortOut(int targetPort) throws IOException {
        setOscPortOut(hostName, targetPort);
    }

    //Call this when you need to change the hostname (will reuse current port number)
    public void setOscPortOut(String hostName) throws IOException {
        setOscPortOut(hostName, targetPort);
    }

    //Call this when you need to change the hostname and targetPort
    public void setOscPortOut(String hostName, int targetPort) throws IOException {
        oscPortOut.disconnect();
        oscPortOut.close();
        oscPortOut = new OSCPortOut(InetAddress.getByName(hostName), targetPort);
        setHostName(hostName);
        setTargetPort(targetPort);
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
