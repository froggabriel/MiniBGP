package com.redes.tp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    Boolean routerStarted;
    HashMap<String, ArrayList<String>> routeMap;
    Thread router;

    public static void main(String[] args) {
        new Main().showMenu();
    }

    private void showMenu() {
        routerStarted = false;
        routeMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        String command;
        while((command = scanner.nextLine().toLowerCase()).compareTo("exit") != 0) {
            String[] words = command.split(" ");
            if(words[0].compareTo("start") == 0) {
                if(routerStarted) {
                    System.out.println("Router has already been started");
                }
                else {
                    router = new Thread(new Router(routeMap));
                    router.start();
                }
            }
            else if(words[0].compareTo("stop") == 0) {
                router.interrupt();
            }
            else if(words[0].compareTo("add") == 0) {

            }
            else if(command.compareTo("show routes") == 0) {
                routeMap.toString(); //print
            }
            else {
                System.out.println("Command not recognized");
            }
        }
    }
}
