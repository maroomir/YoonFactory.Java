package com.yoonsample.testcomm;

import com.yoonfactory.comm.IYoonComm;
import com.yoonfactory.comm.YoonServer;
import com.yoonfactory.log.YoonConsoler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static YoonConsoler pCLM = new YoonConsoler();
    public static IYoonComm pComm;
    public static void main(String args[]) throws IOException {
        System.out.println("Typing the module (TCPServer, TCPClient, Serial) >> ");
        BufferedReader pReader = new BufferedReader(new InputStreamReader(System.in));
        String strSelectedModule = pReader.readLine();
        switch(strSelectedModule.toLowerCase()) {
            case "tcpserver":
                pCLM.write("Start TCPServer Module");
                ProcessServer();
                break;
            case "tcpclient":
                pCLM.write("Start TCPClient Module");
                ProcessClient();
                break;
            case "serial":
                pCLM.write("Start Serial Module");
                ProcessSerial();
                break;
            default:
                pCLM.write("Interrupt Module");
                break;
        }
    }

    public static void ProcessServer() {
        //
    }

    public static void ProcessClient() {
        //
    }

    public static void ProcessSerial() {
        //
    }
}
