package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String input;
            while ((input = in.readLine()) != null) {
                System.out.println("Server: " + input);
                out.println(input);

                input = stdIn.readLine();
                if (input != null) {
                    System.out.println("Client: " + input);
                }
            }
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
