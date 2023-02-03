package com.SitStayCreate.CerealOSC;

import com.SitStayCreate.CerealOSC.RequestServer.RequestServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args){
        //Create the request server when the program opens
        RequestServer requestServer = new RequestServer();
        requestServer.startServer();
        System.out.println("Press Enter to terminate server");
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(System.in));
        try {
            bufferedReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
