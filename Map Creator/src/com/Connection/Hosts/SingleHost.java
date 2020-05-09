package com.Connection.Hosts;

import com.methods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SingleHost extends Host {
    protected Socket socket;
    protected BufferedReader stdIn;
    protected String message = null;
    protected int disconnectTime = 50;
    protected int disconnectTimer = disconnectTime;

    public boolean connect(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        try {
            return connect(new Socket(hostName, portNumber));
        }
        catch (Exception e) {
            endThread(e, "connect()");
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
            endThread(e, "connect()");
            return false;
        }
    }

    public void setMessage(String message) {
        this.message = message;
        System.out.println(this.message);
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

    public void lockToBottom() {
        if (logList != null) {
            logList.setSelectedIndex(connectionLog.size() - 1);
            logList.ensureIndexIsVisible(logList.getSelectedIndex());
            frame.repaint();
        }
    }
    public void sendMessage(String message) {
        super.sendMessage(message);
        lockToBottom();
    }
    public int sendMessage(int i) {
        sendMessage(Integer.toString(i));
        return i + 1;
    }
    public String receiveMessage() {
        String message = super.receiveMessage();
        lockToBottom();
        return message;
    }
    public boolean sendAndReceive() {
        return false;
    }

    public boolean disconnect(String message) {
        if (message != null) {
            disconnectTimer = disconnectTime;
            if (message.contains("Connection closed")) {
                closeThread();
                return true;
            }
        }
        return false;
    }

    public boolean timedown() {
        if (disconnectTimer < 1) {
            closeThread();
            return false;
        }
        disconnectTimer -= 1;
        return true;
    }
    public void run() {
        while (canRun()) {
            if (connectionLog != null) {
                while (in != null) {
                    sendAndReceive();
                    System.out.println();
                }
                endThread("run()");
            }
        }
        System.out.println(methods.tuple("INTERRUPT_THREAD", this));
        getThread().interrupt();
    }

    public void closeThread() {
        addLog(out, "Connection closed.");
        endThread("closeThread()");
    }
    public void endThread(Exception e, String reason) {
        super.endThread(e, reason);
    }
}
