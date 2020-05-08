package com.Connection.Hosts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SingleHost extends Host {
    protected Socket socket;
    protected BufferedReader stdIn;
    protected String message = null;

    public boolean connect(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        try {
            return connect(new Socket(hostName, portNumber));
        }
        catch (Exception e) {
            endThread(e);
            return false;
        }
    }
    public boolean connect(Socket socket) {
        try {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            return true;
        }
        catch (Exception e) {
            endThread(e);
            return false;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setMessage() {
        setMessage(null);
    }
    public String getMessage() {
        return message;
    }
    public boolean holdingMessage() {
        return (message != null);
    }

    public int sendMessage(int i) {
        sendMessage(Integer.toString(i));
        return i + 1;
    }
    public void sendMessage(String message) {
        addLog(out, message);
        setMessage();
    }

    public void recieveMessage() {
        String input;
        try {
            if ((input = in.readLine()) != null) {
                addLog(input);
                if (input.contains("ERR")) {
                    addLog(out, "Error Received.");
                }
                if (input.equals("Connection closed.")) {
                    endThread();
                }
            }
        }
        catch (IOException e) {
            endThread(e);
        }
    }

    public void sendAndRecieve() {
        if (message != null) {
            System.out.println(message);
            sendMessage(message);
        }
        recieveMessage();
    }

    public void run() {
        while (canRun()) {
            System.out.println(getClass().getSimpleName() + ": " + connectionLog);
            if (!(connectionLog == null || in == null)) {
                sendAndRecieve();
            }
        }
        getThread().interrupt();
    }

    public void endThread(Exception e) {
        super.endThread(e);
    }
}