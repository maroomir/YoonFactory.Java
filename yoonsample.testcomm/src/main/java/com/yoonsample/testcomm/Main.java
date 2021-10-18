package com.yoonsample.testcomm;

import com.yoonfactory.comm.IYoonTcpIp;
import com.yoonfactory.comm.YoonServer;
import com.yoonfactory.log.YoonConsoler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static YoonConsoler pCLM = new YoonConsoler();
    public static IYoonTcpIp pComm;
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
            default:
                pCLM.write("Interrupt Module");
                break;
        }
    }

    public static void ProcessServer() throws IOException {
        BufferedReader pReader;
        System.out.println("Address >> ");
        pReader = new BufferedReader(new InputStreamReader(System.in));
        String strAddress = pReader.readLine();
        System.out.println("Port >> ");
        pReader = new BufferedReader(new InputStreamReader(System.in));
        String strPort = pReader.readLine();
        pComm = new YoonServer();
        pComm.setAddress(strAddress);
        pComm.setPort(Integer.parseInt(strPort));
        pComm.open();
        System.out.println("Send >> ");
        pReader = new BufferedReader(new InputStreamReader(System.in));
        pComm.send(pReader.readLine());
    }

    public static void ProcessClient() throws IOException {
        BufferedReader pReader;
        System.out.println("Address >> ");
        pReader = new BufferedReader(new InputStreamReader(System.in));
        String strAddress = pReader.readLine();
        System.out.println("Port >> ");
        pReader = new BufferedReader(new InputStreamReader(System.in));
        String strPort = pReader.readLine();
        pComm = new YoonServer();
        pComm.setAddress(strAddress);
        pComm.setPort(Integer.parseInt(strPort));
        pComm.open();
        System.out.println("Send >> ");
        pReader = new BufferedReader(new InputStreamReader(System.in));
        pComm.send(pReader.readLine());
    }
}
