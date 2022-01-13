package com.SitStayCreate.Serialosc;

import com.illposed.osc.*;
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector;
import com.illposed.osc.transport.udp.OSCPortIn;
import com.illposed.osc.transport.udp.OSCPortOut;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestServer {
    //Listens on this port for requests
    private static final int PORT_NUMBER = 12002;
    private OSCPortIn oscPortIn;
    private OSCPortOut oscPortOut;
    //List of monomeApps that want to be updated when a new device connects (/serialosc/notify)

    private Set<MonomeApp> monomeApps;

    //List of oscDevices
    //TODO: Make this a set - look at how equality works with children
    private List<GridController> gridControllers;

    //I think we just create a new one and don't do anything special
    public RequestServer(){
        monomeApps = new HashSet<>();
        gridControllers = new ArrayList<>();
    }


    public void addMonomeController(GridController gridController) {
        gridControllers.add(gridController);
    }

    public List<GridController> getGridControllers() {
        return gridControllers;
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
    public void notifyListeners(MonomeController monomeController){

        //Get the controller's input port
        DecoratedOSCPortIn decoratedOSCPortIn = monomeController.getDecoratedOSCPortIn();

        //build oscArgs
        ArrayList oscArgs = new ArrayList();
        oscArgs.add(monomeController.getId());
        oscArgs.add("monome " + ((GridController)monomeController).getDimensions().getArea());
        oscArgs.add(decoratedOSCPortIn.getPortIn());
        //send response to each MonomeApp in the list
        for(MonomeApp monomeApp : monomeApps){
            sendResponse(monomeApp, "/serialosc/device", new OSCMessageInfo("ssi"), oscArgs);
        }
    }

    //Helper methods for event listeners
    public void sendResponse(MonomeApp monomeApp, String addressString, OSCMessageInfo oscMessageInfo, List oscArgs){
        try{
            //Open port out to send response
            oscPortOut = new OSCPortOut(monomeApp.getInetAddress(), monomeApp.getPortNumber());
            //Create and send response
            OSCMessage oscMessage = new OSCMessage(addressString, oscArgs, oscMessageInfo);
            //Send response to target monomeApp - this is how target monomeApp knows where to send stuff
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

    //Sends list of devices to an monomeApp
    private class ListMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            //Get the args from the message which are [host address, port]
            String hostName = (String) event.getMessage().getArguments().get(0);
            int portNumber = (int) event.getMessage().getArguments().get(1);

            MonomeApp monomeApp = new MonomeApp(hostName, portNumber);

            //If oscDevices has no items, we have nothing to send
            if(gridControllers.size() == 0){
                return;
            }

            for (GridController gridController : gridControllers){
                DecoratedOSCPortIn decoratedOSCPortIn = gridController.getDecoratedOSCPortIn();
                //build up oscArgs
                ArrayList oscArgs = new ArrayList();
                oscArgs.add(gridController.getId());
                oscArgs.add("monome " + gridController.getDimensions().getArea()); //used to be Dimensions class method GetArea()
                oscArgs.add(decoratedOSCPortIn.getPortIn());

                sendResponse(monomeApp,
                        "/serialosc/device",
                        new OSCMessageInfo("ssi"),
                        oscArgs);
            }
        }
    }

    //Adds monomeApp to list of listening monomeApps
    private class NotifyMessageListener implements OSCMessageListener {
        public void acceptMessage(OSCMessageEvent event) {
            List oscArgs = event.getMessage().getArguments();
            String hostName = (String) oscArgs.get(0);
            int portNumber = (int) oscArgs.get(1);
            monomeApps.add(new MonomeApp(hostName, portNumber));
        }
    }

}
