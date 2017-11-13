package com.redes.tp2;

import java.util.ArrayList;
import java.util.HashMap;

public class Router implements Runnable{
    private HashMap<String, ArrayList<String>> routeMap;
    private HashMap<String, String> knownAS; //AS, IP
    private String as;

    Router(HashMap<String, ArrayList<String>> routeMap) {
        this.routeMap = routeMap;
        this.as = "AS8";
    }

    @Override
    public void run() {
        new Thread(new Server(as, 8888, routeMap, knownAS)).start();
        new Thread(new Client(as, "127.0.0.1", 8889, routeMap, knownAS)).start();
        new Thread(new Server(as, 8889, routeMap, knownAS)).start();
       
    }
}
