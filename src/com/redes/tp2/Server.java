package com.redes.tp2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Server extends Connection {
    private int port;

    Server(String as, int port, HashMap<String, ArrayList<String>> routeMap, HashMap<String, String> knownAS, String initialRoutes) {
        this.as = as;
        this.port = port;
        this.routeMap = routeMap;
        this.knownAS = knownAS;
        this.initalRoutes = initialRoutes;
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            System.out.println("A client has connected to the server...");
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            // Para los canales de input y output de datos

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inputThread = new Thread(new InputThread(input));
            inputThread.start();

            output = new DataOutputStream(socket.getOutputStream());
            outputThread = new Thread(new OutputThread(output));
            outputThread.start();
        }catch(IOException e) {
            e.printStackTrace();
        }

        try{
            Thread.sleep(60000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        try{
            output.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error de input/output." + e.getMessage());
        }


    }
}
