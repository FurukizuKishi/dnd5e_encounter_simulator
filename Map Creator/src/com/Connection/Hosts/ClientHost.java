package com.Connection.Hosts;

import com.Connection.JoinSessionGUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHost extends Host {
    public ClientHost(JoinSessionGUI frame, String hostName, int portNumber) {
        this.frame = frame;
        this.hostName = hostName;
        this.portNumber = portNumber;
        connect(hostName, portNumber);
    }

    public void connect(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;

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
        catch (Exception e) {
            System.out.println(e);
            frame.connectButton.setEnabled(true);
            frame.hostField.setEditable(true);
            frame.portNumber.setEditable(true);
        }
    }
}
