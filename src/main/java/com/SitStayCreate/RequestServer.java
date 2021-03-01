package com.SitStayCreate;

import com.illposed.osc.*;
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector;
import com.illposed.osc.transport.udp.OSCPortIn;
import com.illposed.osc.transport.udp.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class RequestServer {
    //Listens on this port for requests
    private static final int PORT_NUMBER = 12002;
    private OSCPortIn oscPortIn;
    private OSCPortOut oscPortOut;
    //List of apps that want to be updated when a new device connects (/serialosc/notify)
    private List<App> apps;

    //List of oscDevices
    private List<OscDevice> oscDevices;

    //I think we just create a new one and don't do anything special
    public RequestServer(){
        apps = new ArrayList<>();
        oscDevices = new ArrayList<>();
    }


    public void addOscDevice(OscDevice oscDevice) {
        oscDevices.add(oscDevice);
    }

    public List<OscDevice> getOscDevices() {
        return oscDevices;
    }

    public void startServer(){
        try{
            oscPortIn = new OSCPortIn(PORT_NUMBER);

            //Create Listeners for the port
            String listString = "/serialosc/list";
            MessageSelector listSelector = new OSCPatternAddressMessageSelector(listString);
            //Create an event listener
            ListMessageListener listMessageListener = new ListMessageListener();
            oscPortIn.getDispatcher().addListener(listSelector, listMessageListener);

            //Create Listeners for the port
            String notifyString = "/serialosc/notify";
            MessageSelector notifySelector = new OSCPatternAddressMessageSelector(notifyString);
            //Create an event listener
            NotifyMessageListener notifyMessageListener = new NotifyMessageListener();
            oscPortIn.getDispatcher().addListener(notifySelector, notifyMessageListener);
            oscPortIn.startListening();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    //send list of OscDevices when a new device is added
    public void notifyListeners(OscDevice oscDevice){
        for(App app : apps){
            //build oscArgs
            ArrayList oscArgs = new ArrayList();
            oscArgs.add(oscDevice.getId());
            oscArgs.add(oscDevice.getDimensions().getArea());
            oscArgs.add(oscDevice.getOscPortInWrapper().getPortInNumber());

            sendResponse(app, "/serialosc/device", new OSCMessageInfo("ssi"), oscArgs);
        }
    }

    //Helper methods for event listeners
    public void sendResponse(App app, String addressString, OSCMessageInfo oscMessageInfo, List oscArgs){
        try{
            //Open port out to send response
            oscPortOut = new OSCPortOut(app.getInetAddress(), app.getPortNumber());
            //Create and send response
            OSCMessage oscMessage = new OSCMessage(addressString, oscArgs, oscMessageInfo);
            //Send response to target app - this is how target app knows where to send stuff
            oscPortOut.send(oscMessage);
            oscPortOut.close();

        } catch (UnknownHostException ex){
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        } catch (OSCSerializeException ex){
            ex.printStackTrace();
        }
    }

    //Sends list of devices to an app
    private class ListMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            //Get the args from the message which are [host address, port]
            String ipv4AddressString = (String) event.getMessage().getArguments().get(0);
            int portNumber = (int) event.getMessage().getArguments().get(1);

            try{
                App app = new App(InetAddress.getByName(ipv4AddressString), portNumber);

                //If oscDevices has no items, we have nothing to send
                if(oscDevices.size() == 0){
                    return;
                }

                for (OscDevice oscDevice : oscDevices){
                    //build up oscArgs
                    ArrayList oscArgs = new ArrayList();
                    oscArgs.add(oscDevice.getId());
                    oscArgs.add(oscDevice.getDimensions().getArea());
                    oscArgs.add(oscDevice.getOscPortInWrapper().getPortInNumber());

                    sendResponse(app,
                            "/serialosc/device",
                            new OSCMessageInfo("ssi"),
                            oscArgs);
                }

            } catch (UnknownHostException ex){
                ex.printStackTrace();
            }
        }
    }

    //Adds app to list of listening apps
    private class NotifyMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List oscArgs = event.getMessage().getArguments();
            String hostName = (String) oscArgs.get(0);
            int portNumber = (int) oscArgs.get(1);

            try{
                InetAddress inetAddress = InetAddress.getByName(hostName);
                apps.add(new App(inetAddress, portNumber));
            } catch (UnknownHostException ex){
                ex.printStackTrace();
            }

        }
    }

}
