package com.redes.tp2;

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

    }
}
