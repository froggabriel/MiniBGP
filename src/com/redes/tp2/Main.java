package com.redes.tp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    Boolean routerStarted;
    HashMap<String, ArrayList<String>> routeMap;
    HashMap<String, String> knownAS;
    Thread router;

    public static void main(String[] args) {
        new Main().showMenu();
    }

    private void showMenu() {
        routerStarted = false;
        routeMap = new HashMap<>();
        knownAS = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        String command;
        while((command = scanner.nextLine().toLowerCase()).compareTo("exit") != 0) {
            String[] words = command.split(" ");
            if(words[0].compareTo("start") == 0) {
                if(routerStarted) {
                    System.out.println("Router has already been started");
                }
                else {
                    router = new Thread(new Router(routeMap, knownAS));
                    router.start();
                }
            }
            else if(words[0].compareTo("stop") == 0) {
                router.interrupt();
            }
            else if(words[0].compareTo("add") == 0) {

            }
            else if(command.compareTo("show routes") == 0) {
                Iterator<String> it = knownAS.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    System.out.println("red " + key + " ->  " + knownAS.get(key));
                }
            }
            else {
                System.out.println("Command not recognized");
            }
        }
    }
}
