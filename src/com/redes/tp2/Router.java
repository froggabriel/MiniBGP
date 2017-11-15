package com.redes.tp2;

import java.util.ArrayList;
import java.util.HashMap;

public class Router implements Runnable{
    private HashMap<String, ArrayList<String>> routeMap;
    private HashMap<String, String> knownAS; //AS, IP
    private String as;

    Router(HashMap<String, ArrayList<String>> routeMap, HashMap<String, String> knownAS) {
        this.routeMap = routeMap;
        this.knownAS = knownAS;
        this.as = "AS8";
    }

    @Override
    public void run() {
        //new Thread(new Server(as, 8888, routeMap, knownAS)).start();
        new Thread(new Client("AS8", "127.0.0.1", 8889, routeMap, knownAS, "AS8*192.168.1.7:AS8-AS7\n")).start();
        new Thread(new Server("AS9", 8889, routeMap, knownAS, "AS9*192.168.1.6:AS9-AS6\n")).start();
    }
}
