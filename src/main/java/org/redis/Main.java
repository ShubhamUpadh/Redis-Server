package org.redis;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the port number for the server (default 1234)");
        final int port = sc.nextInt();
        
        if (port < 0 || port > 65535) System.out.println("Invalid port number ");
        
        NetworkLayer networkLayer = new NetworkLayer(port);
    }
}