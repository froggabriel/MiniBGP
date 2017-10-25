package com.redes.tp2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client implements Runnable {
    private String as;
    private String ip;
    private int port;
    private HashMap<String, ArrayList<String>> routeMap;

    Client(String as, String ip, int port, HashMap<String, ArrayList<String>> routeMap) {
        this.as = as;
        this.ip = ip;
        this.port = port;
        this.routeMap = routeMap;
    }

    @Override
    public void run() {
    	 Socket socket;
         DataOutputStream mensaje;
         String msjInicial="AS0*192.168.0.0:AS0,AS-1,AS-2";
          
         try {
             //Creamos nuestro socket
             socket = new Socket(ip, port);
      
             mensaje = new DataOutputStream(socket.getOutputStream());
  
             //Enviamos un mensaje
             mensaje.writeUTF(msjInicial);
  
             //Cerramos la conexión
             socket.close();
  
         } catch (UnknownHostException e) {
             System.out.println("El host no existe o no está activo.");
         } catch (IOException e) {
             System.out.println("Error de entrada/salida.");
         }
    }
}
