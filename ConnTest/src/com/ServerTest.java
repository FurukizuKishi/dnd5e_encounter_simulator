package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.print("Attempting to establish a connection... ");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection established.");

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("Connection established.");

            System.out.println();
            String input;
            while ((input = in.readLine()) != null) {
                out.println(input);
                if (input.equals("bye")) {
                    break;
                }
            }
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
