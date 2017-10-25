package com.redes.tp2;

import java.util.ArrayList;
import java.util.HashMap;

public class Server implements Runnable{
    private String as;
    private int port;
    private HashMap<String, ArrayList<String>> routeMap;

    Server(String as, int port, HashMap<String, ArrayList<String>> routeMap) {
        this.as = as;
        this.port = port;
        this.routeMap = routeMap;
    }

    @Override
    public void run() {
    
    }
}
