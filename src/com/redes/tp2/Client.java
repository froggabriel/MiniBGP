package com.redes.tp2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Client extends Connection {
    private String ip;
    private int port;

    Client(String as, String ip, int port, HashMap<String, ArrayList<String>> routeMap, HashMap<String, String> knownAS) {
        this.as = as;
        this.ip = ip;
        this.port = port;
        this.routeMap = routeMap;
        this.knownAS = knownAS;
    }

    @Override
    public void run() {
        boolean connected = false;
        while (!connected) {
            try {
                //Creamos nuestro socket
                socket = new Socket(ip, port);
                connected = true;
            } catch (IOException e) {
                connected = false;
            }
        }

        try {
            output = new DataOutputStream(socket.getOutputStream());
            this.outputThread = new Thread(new OutputThread(output));
            outputThread.start();

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.inputThread = new Thread(new InputThread(input));
            inputThread.start();
        } catch (IOException e) {
            System.out.println("Error de input/output.");
        }

        try {
            Thread.sleep(30000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        boolean closed = false;
        while(!closed) {
            try {
                socket.close();
                closed = true;
            } catch(IOException e) {
                closed = false;
            }
        }
    }
}
