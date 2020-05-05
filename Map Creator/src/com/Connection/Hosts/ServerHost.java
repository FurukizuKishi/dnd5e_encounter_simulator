package com.Connection.Hosts;

import com.Connection.ActionCommand;
import com.Connection.ActionSignalProtocol;
import com.Connection.CreateSessionGUI;
import com.methods;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ServerHost extends Host {
    private ServerSocket serverSocket;
    private ArrayList<ServerThreadHost> clients = new ArrayList<>();
    private ActionSignalProtocol protocol;
    public Queue<ActionCommand> queue = new LinkedList<>();
    public ServerHost(CreateSessionGUI frame, int portNumber) {
        this.frame = frame;
        this.hostName = "localhost";
        this.portNumber = portNumber;
        try {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            endThread(e);
        }
    }

    public void connect(ServerSocket socket) {
        try {
            System.out.print("Attempting to establish a connection... ");
            clients.add(new ServerThreadHost((CreateSessionGUI) frame, this, socket.accept()));
            System.out.println("Connection established.");
        }
        catch (Exception e) {
            endThread(e);
        }
    }

    public void startClients() {
        for (int i = 0; i < clients.size(); i += 1) {
            clients.get(i).startThread();
        }
    }
    public void endClients() {
        int n = clients.size();
        for (int i = 0; i < n; i += 1) {
            endClient(clients.get(0));
        }
    }
    public void endClient(ServerThreadHost client) {
        client.addLog(client.out, "Connection closed.");
        System.out.println("Connection closed.");
        client.endThread();
        if (clients.contains(client)) {
            ((CreateSessionGUI) frame).connectionTabs.remove(clients.indexOf(client));
            clients.remove(client);
        }
    }

    public void startThread() {
        protocol = new ActionSignalProtocol(this);
        super.startThread();
        startClients();

    }
    public void endThread(Exception e) {
        super.endThread(e);
        try {
            endClients();
            protocol.getThread().end();
            serverSocket.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addCommand(ServerThreadHost host, String command) {
        queue.add(new ActionCommand(host, command));
    }

    public void sendToThread(ActionCommand command) {
        command.host.setMessage("Processed command '" + command.command + "'.");
    }

    public void run() {
        while (canRun()) {
            connect(serverSocket);
        }
        getThread().interrupt();
    }
}
