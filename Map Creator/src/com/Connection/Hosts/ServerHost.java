package com.Connection.Hosts;

import com.Connection.CreateSessionGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerHost extends Host {
    private ServerSocket serverSocket;
    private ArrayList<ServerThreadHost> clients = new ArrayList<>();
    public ServerHost(CreateSessionGUI frame, int portNumber) {
        this.frame = frame;
        this.hostName = "localhost";
        this.portNumber = portNumber;
        connect(portNumber);
    }

    public void connect(int portNumber) {
        this.portNumber = portNumber;

        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.print("Attempting to establish a connection... ");
            clients.add(new ServerThreadHost((CreateSessionGUI) frame, serverSocket.accept()));
            System.out.println("Connection established.");

            addLog(out, "Connection established.");
        }
        catch (IOException e) {
            endThread(e);
        }
    }

    public void endThread(Exception e) {
        super.endThread(e);
        try {
            addLog(out, "Connection closed.");
            for (int i = 0; i < clients.size(); i += 1) {
                clients.get(0).endThread();
                serverSocket.close();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        frame.connectButton.setEnabled(true);
        frame.portNumber.setEditable(true);
    }

    public void run() {
        while (canRun()) {
            if (frame.connectionLog != null) {
                try {
                    System.out.println();
                    String input;
                    while (in != null) {
                        if ((input = in.readLine()) != null) {
                            addLog(out, input);
                            if (input.equals("bye")) {
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    addLog(out, "[ERR]: " + e.getMessage());
                    endThread(e);
                }
            }
        }
    }
}
