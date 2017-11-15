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
    HashMap<String, ArrayList<String>> routeMap;
    HashMap<String, String> knownAS;
    String as;
    BufferedReader input;
    DataOutputStream output;
    Socket socket;
    ServerSocket serverSocket;
    Thread inputThread;
    Thread outputThread;
    String initalRoutes;

    public void rutasConocidas() {
        Iterator<String> it = routeMap.keySet().iterator();
        String r = "";
        while (it.hasNext()) {
            String key = (String) it.next();
            ArrayList<String> rutas = routeMap.get(key);
            for (int i = 0; i < rutas.size(); i++) {
                r = rutas.get(i);// substraer solo la subred
                int dosp = 0;
                for (int j = 0; j < r.length(); j++) {
                    if (r.charAt(j) == ':') {
                        dosp = j;
                        break;
                    }
                }

                String subred = r.substring(0, dosp); // aqui ya esta solo la subred
                String listaAS = r.substring(dosp + 1);
                //if (!knownAS.containsKey(r)) {
                knownAS.put(subred, listaAS);
            }
        }
    }

    public int getCantAS(String s) {
        int c = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'A') {
                c++;
            }
        }
        return c;
    }

    public void printRutasConocidas() {
        Iterator<String> it = knownAS.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            System.out.println("red " + key + " ->  " + knownAS.get(key));
        }
    }

    String getKnownRoutes() {
        String routes = as + "*";
        for (String key : knownAS.keySet()) {
            routes = routes.concat(key + ":" + knownAS.get(key) + ",");
        }
        routes = routes.concat("\n");
        return routes;
    }

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
        String ast = m.substring(asterisco); // ast = del asterisco en adelante
        String newIdAs = as + ast; // se le agrega de primero el actual as

        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == ':') {
                dosp = i;
                break;
            }
        }
        String asAnteriores = m.substring(dosp + 1); // AS#,Subred:AS#,
        // A newIdAs se le quita la parte del resto de las As
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

            if(asterisco < s.length() - 1) {

                String nombreAs = s.substring(0, asterisco); // solo el nombre de la AS que lo envia
                String resto = s.substring(asterisco + 1);
                String[] r = resto.split(",");
                ArrayList<String> rutas = new ArrayList<>();
                for (int i = 0; i < r.length; i++) {
                    // verificar que no hayan ciclos en las rutas
                    String ruta = r[i];
                    String comprobacion1 = as + "-";
                    String comprobacion2 = "-" + as;
                    if (!ruta.contains(comprobacion1) && !ruta.contains(comprobacion2)) {
                        rutas.add(r[i]);
                    }
                }
                routeMap.put(nombreAs, rutas);
            }
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
                    //System.out.println(line);
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
            boolean initWritten = false;
            while(!initWritten) {
                try {
                    output.writeUTF(initalRoutes);
                    initWritten = true;
                } catch (IOException e) {
                    initWritten = false;
                }
            }

            String routes = "";
            while(true) {
                synchronized (knownAS) {
                    rutasConocidas();
                }
                routes = getKnownRoutes();

                if(!routes.equals("")) {
                    boolean written = false;
                    while (!written) {
                        try {
                            //System.out.println("AS0*192.168.0.0:AS0-AS1-AS2\n");
                            output.writeUTF(routes);//routes);
                            written = true;
                            Thread.sleep(10000);
                        } catch (IOException e) {
                            written = false;
                        } catch (InterruptedException e) {
                            System.out.println("output connection closed");
                        }
                    }
                }
            }
        }
    }
}
