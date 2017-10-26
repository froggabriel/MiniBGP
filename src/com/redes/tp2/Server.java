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

public class Server implements Runnable{
    private String as;
    private int port;
    private HashMap<String, ArrayList<String>> routeMap;
   

    Server(String as, int port, HashMap<String, ArrayList<String>> routeMap) {
        this.as = as;
        this.port = port;
        this.routeMap = routeMap;
    }
    
    String updateMsj(String m) {
    	int dosp=0; int asterisco=0;
    	//encuentra las posiciones que se necesitan (* y :)
    	for(int i=0;i<=m.length();i++) {
    		if(m.charAt(i)==':') { dosp = i; }
    		if(m.charAt(i)=='*') {asterisco = i;}
    	}
    	//se agrega la As actual a la secuencia de As's
    	String b = m.substring(dosp+1);   //AS#,Subred:AS#,
    	String a = m.substring(0,dosp);  // ID*Subred:
    	String z= as +b+',';
    	z = a + z; // se une todo el msj con el As actual en el medio
    	//se elimina el numero de AS anterior y se pone el actual al principio del msj
    	String u =z.substring(asterisco);
        u= as + z;
    	
    	return u;
    }
    
    public void addRouteMap(String s) {
      	
      	int asterisco =0;
      	for(int i=0;i<=s.length();i++) {
      		if(s.charAt(i)=='*') {asterisco = i; break;}
      	}
      	String nombreAs= s.substring(0,asterisco);  //solo el nombre de la AS que lo envia
      	String resto =s.substring(asterisco+1); 
      	String[] r = resto.split(",");
      	ArrayList<String> rutas = new ArrayList<>();
      	for(int i=0;i<r.length;i++) {
      		rutas.add(r[i]);
      	}
      	routeMap.put(nombreAs,rutas );
      	
      }
    
    public void printRouteMap() {
  	  Iterator it = routeMap.keySet().iterator();
    	while(it.hasNext()){
    	  String key = (String) it.next();
    	 ArrayList<String> rutas = routeMap.get(key);
    	 for(int i=0;i< rutas.size();i++) {
    	  System.out.println("Clave: " + key + " -> Valor: " + rutas.get(i));
    	 }
    	}
    	
    }

    @Override
    public void run() {
    	
    	BufferedReader entrada;
        DataOutputStream salida;
        Socket socket;
        ServerSocket serverSocket;
         
        try {
            serverSocket = new ServerSocket(port);
 
            System.out.println("Esperando una conexión...");
            
 
            socket = serverSocket.accept();
 
            System.out.println("Un cliente se ha conectado...");
 
            // Para los canales de entrada y salida de datos
 
            entrada = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            
            
            System.out.println("readline original"+entrada.readLine());
            
            addRouteMap(entrada.readLine());//agrega las rutas entrantes al routemap
            
           // String msjActualizado= updateMsj(entrada.readLine());
            
            //System.out.println("msj actualizado" +msjActualizado);
 
            salida = new DataOutputStream(socket.getOutputStream());
 
            System.out.println("Confirmando conexion al cliente....");
 
            salida.writeUTF("Conexión exitosa...");
 
            // Para recibir el mensaje
 
            String mensajeRecibido = entrada.readLine();
 
            System.out.println(mensajeRecibido);
 
            salida.writeUTF("Se recibio tu mensaje.");
 
            salida.writeUTF("Gracias por conectarte.");
 
            System.out.println("Cerrando conexión...");
 
            // Cerrando la conexón
            serverSocket.close();
            
 
        } catch (IOException e) {
            System.out.println("Error de entrada/salida."  + e.getMessage());
        }
 
    
    }
}
