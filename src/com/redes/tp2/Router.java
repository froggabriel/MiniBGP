package com.redes.tp2;

import java.util.ArrayList;
import java.util.HashMap;

public class Router implements Runnable{
    private HashMap<String, ArrayList<String>> routeMap;
    private String as;

    Router(HashMap<String, ArrayList<String>> routeMap) {
        this.routeMap = routeMap;
        this.as = "51";
    }

    @Override
    public void run() {
        new Thread(new Server(as, 8888, routeMap)).start();
        new Thread(new Client(as, "127.0.0.1", 8889, routeMap)).start();
    }
}
