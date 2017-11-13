package com.redes.tp2;

import jdk.internal.util.xml.impl.Input;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Connection implements Runnable {
    protected HashMap<String, ArrayList<String>> routeMap;
    protected HashMap<String, String> knownAS;
    protected String as;
    protected BufferedReader input;
    protected DataOutputStream output;
    protected Socket socket;
    protected ServerSocket serverSocket;
    protected Thread inputThread;
    protected Thread outputThread;

    String updateMsj(String m) {
        int dosp = 0;
        int asterisco = 0;
        // encuentra las posiciones que se necesitan (* y :)
        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == '*') {
                asterisco = i;
                break;
            }
        }
        String ast = m.substring(asterisco); //ast = del asterisco en adelante
        String newIdAs = as + ast; //se le agrega de primero el actual as

        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == ':') {
                dosp = i;
                break;
            }
        }
        String asAnteriores = m.substring(dosp + 1); // AS#,Subred:AS#,
        //A newIdAs se le quita la parte del  resto de las As
        newIdAs = newIdAs.substring(0, dosp);
        newIdAs = newIdAs + "AS" + as + ",";
        String update = newIdAs + asAnteriores;

        return update;
    }

    public void addRouteMap(String s) {
        if (s == null) {
            System.out.println("Ruta vacia");
        } else {

            int asterisco = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '*') {
                    asterisco = i;
                    break;
                }
            }

            String nombreAs = s.substring(0, asterisco); // solo el nombre de la AS que lo envia
            String resto = s.substring(asterisco + 1);
            String[] r = resto.split(",");
            ArrayList<String> rutas = new ArrayList<>();
            for (int i = 0; i < r.length; i++) {
                rutas.add(r[i]);
            }
            routeMap.put(nombreAs, rutas);
        }

    }

    public void printRouteMap() {
        Iterator<String> it = routeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            ArrayList<String> rutas = routeMap.get(key);
            for (int i = 0; i < rutas.size(); i++) {
                System.out.println("#AS: " + key + " -> direccion: " + rutas.get(i));
            }
        }

    }

    @Override
    public void run() {

    }

    class InputThread implements Runnable {
        BufferedReader input;

        InputThread(BufferedReader input) {
            this.input = input;
        }

        @Override
        public void run() {
            while (true) {
                boolean read = false;
                String line = null;
                while (!read) {
                    try {
                        line = input.readLine();
                        read = true;
                    } catch (IOException e) {
                        read = false;
                    }
                }
                if (line != null) {
                    System.out.println("readline original" + line);
                    addRouteMap(line);//agrega las rutas entrantes al routemap
                }
            }
        }
    }

    class OutputThread implements Runnable {
        DataOutputStream output;

        OutputThread(DataOutputStream output) {
            this.output = output;
        }

        @Override
        public void run() {
            while(true) {
                String routes;

                ///TODO construir rutas

                boolean written = false;
                while (!written) {
                    try {
                        output.writeUTF("hola");//routes);
                        written = true;
                    } catch (IOException e) {
                        written = false;
                    }
                }
            }
        }
    }
}
