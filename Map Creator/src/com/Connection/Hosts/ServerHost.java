package com.Connection.Hosts;

import com.Connection.Action.ActionCommand;
import com.Connection.Action.ActionSignalProtocol;
import com.Connection.GUI.CreateSessionGUI;
import com.Connection.GUI.JoinSessionGUI;
import com.Game.methods;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerHost extends Host<ActionCommand> {
    private ServerSocket serverSocket;
    private ArrayList<ServerThreadHost> clients = new ArrayList<>();
    private ArrayList<ActionCommand> completedCommands = new ArrayList<>();
    private ActionSignalProtocol protocol;
    public ServerThreadHost master;
    public boolean masterCreated = false;
    public int cx, cy;
    public ServerHost(CreateSessionGUI frame, int portNumber) {
        this.frame = frame;
        cx = frame.getX();
        cy = frame.getY();
        this.hostName = "localhost";
        this.portNumber = portNumber;
        try {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            endThread(e, "port(null)");
        }
    }

    public ArrayList<ServerThreadHost> getClients() {
        return clients;
    }
    public ServerThreadHost getClient(String name) {
        for (ServerThreadHost client : clients) {
            if (client.name.equals(name)) {
                return client;
            }
        }
        return null;
    }

    public void connect(ServerSocket socket) {
        try {
            clients.add(new ServerThreadHost((CreateSessionGUI) frame, this, socket.accept()));
            createProtocol();
        }
        catch (Exception e) {
            endThread(e, "connect()");
        }
    }

    public void startClients() {
        for (int i = 0; i < clients.size(); i += 1) {
            startClient(clients.get(i));
        }
    }
    public void startClient(ServerThreadHost client) {
        client.startThread();
        for (ActionCommand command : completedCommands) {
            ActionCommand copiedCommand = new ActionCommand(command.host, command.command, client.name);
            copiedCommand.setReplaceable(false);
            addCommand(copiedCommand);
        }
    }
    public void endClients() {
        int n = clients.size();
        for (int i = 0; i < n; i += 1) {
            clients.get(0).closeThread();
        }
    }
    public void endClient(ServerThreadHost client) {
        client.endThread("endClient()");
        if (clients.contains(client)) {
            ((CreateSessionGUI) frame).connectionTabs.remove(clients.indexOf(client));
            clients.remove(client);
            frame.repaint();
        }
    }

    public void startThread() {
        super.startThread();
        startClients();
    }
    public void endThread(Exception e, String reason) {
        super.endThread(e, reason);
        try {
            endClients();
            serverSocket.close();
            destroyProtocol();
            if (((CreateSessionGUI) frame).clientFrame != null) {
                cx = ((CreateSessionGUI) frame).clientFrame.getX();
                cy = ((CreateSessionGUI) frame).clientFrame.getY();
                ((CreateSessionGUI) frame).clientFrame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
            frame.closeConnectionLog(frame.w, frame.h);
            frame.changeConnectionButtons(false);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean createProtocol() {
        if (protocol == null) {
            protocol = new ActionSignalProtocol(this);
            return true;
        }
        return false;
    }
    public boolean destroyProtocol() {
        if (protocol != null) {
            protocol.endThread("destroyProtocol()");
            protocol = null;
            return true;
        }
        return false;
    }

    public void addCommand(ServerThreadHost host, String command) {
        addCommand(new ActionCommand(host, command));
    }

    public void sendToThread(ActionCommand command) {
        for (String name : command.recipients) {
            if (name.contains("ALL")) {
                if (name.equals("ALL")) {
                    for (ServerThreadHost currentHost : clients) {
                        sendToHost(currentHost, command);
                    }
                }
                else {
                    String[] exclusions = name.substring("ALL(".length(), name.length() - 1).split(",");
                    //System.out.println(methods.tuple("EXCLUSIONS", exclusions));
                    for (ServerThreadHost currentHost : clients) {
                        if (!methods.arrayToList(exclusions).contains(currentHost.name)) {
                            sendToHost(currentHost, "Processed command \"" + command.result + "\".");
                        }
                        else {
                            sendToHost(currentHost, "Sent the command \"" + command.result + "\".");
                        }
                    }
                }
            }
            else if (name.equals("DM")) {
                sendToHost(master, command);
            }
            else {
                sendToHost(getClient(name), command);
            }
            if (command.isReplaceable()) {
                completedCommands.add(command);
            }
        }
    }

    public void sendToHost(ServerThreadHost host, ActionCommand command) {
        if (host != null) {
            //System.out.println(methods.tuple("SEND", command.result, host, host.name));
            host.sendMessage(command.result);
        }
    }
    public void sendToHost(ServerThreadHost host, String message) {
        if (host != null) {
            //System.out.println(methods.tuple("SEND", message, host, host.name));
            host.sendMessage(message);
        }
    }

    public void setInput(BufferedReader in) {
        this.in = in;
    }

    public String receiveMessage() {
        try {
            String input;
            if ((input = in.readLine()) != null) {
                addLog(input);
                return input;
            }
        }
        catch (Exception e) {
            endThread(e, "receiveMessage()");
        }
        return null;
    }

    @Override
    public void run() {
        if (!masterCreated) {
            ((CreateSessionGUI) frame).clientFrame = new JoinSessionGUI(frame.getFrame(), frame.w, frame.h);
            ((CreateSessionGUI) frame).clientFrame.setLocation(cx, cy);
            ((CreateSessionGUI) frame).clientFrame.hostField.setText("localhost");
            ((CreateSessionGUI) frame).clientFrame.portNumber.setText(Integer.toString(serverSocket.getLocalPort()));
            ((CreateSessionGUI) frame).clientFrame.hostField.setEditable(false);
            ((CreateSessionGUI) frame).clientFrame.portNumber.setEditable(false);
            ((CreateSessionGUI) frame).clientFrame.connectButton.setEnabled(false);
            ((CreateSessionGUI) frame).clientFrame.connect(true);
            masterCreated = true;
        }
        while (canRun()) {
            connect(serverSocket);
        }
        getThread().interrupt();
    }
}
