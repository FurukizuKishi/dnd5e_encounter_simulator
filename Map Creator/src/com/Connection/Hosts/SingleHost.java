package com.Connection.Hosts;

import com.Connection.Threads.ReceiveThread;
import com.Game.methods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SingleHost extends Host<String> {
    protected Socket socket;
    protected ReceiveThread in;
    protected BufferedReader stdIn;
    protected String message = null;
    public String name = null;
    protected boolean messageProcessed = false;
    protected int disconnectTime = 50;
    protected int disconnectTimer = disconnectTime;

    public String toString() {
        if (name != null) {
            return getClass().getSimpleName() + ":" + name;
        }
        return getClass().getSimpleName() + "@" + super.toString().replace(getClass().getCanonicalName() + "@", "");
    }

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
            in = new ReceiveThread(this, new BufferedReader(new InputStreamReader(socket.getInputStream())));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            return true;
        }
        catch (Exception e) {
            endThread(e, "connect()");
            return false;
        }
    }

    public void setInput(BufferedReader in) {
        this.in.setInput(in);
    }
    public boolean isReceiving() {
        return (in.isRunning());
    }

    public void setMessage(String message) {
        this.message = message;
        messageProcessed = false;
        //System.out.println(methods.tuple("MSG_SET"));
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
    public String receiveMessage(String message) {
        if (message != null) {
            addLog(message);
            setMessage(message);
            lockToBottom();
        }
        return message;
    }
    public boolean sendAndReceive() {
        return false;
    }

    public boolean disconnect(String message) {
        if (message != null) {
            disconnectTimer = disconnectTime;
            if (message.toLowerCase().contains("connection closed")) {
                //System.out.println(methods.tuple("CLS", this));
                closeThread();
                return true;
            }
        }
        return false;
    }

    public boolean timedown() {
        if (disconnectTimer < 1) {
            //System.out.println(methods.tuple("TMD", this));
            closeThread();
            return false;
        }
        disconnectTimer -= 1;
        return true;
    }
    public void run() {
        while (canRun()) {
            if (connectionLog != null) {
                while (isReceiving()) {
                    if (connectionLog.size() > 0) {
                        //System.out.println(methods.tuple(60, message, this, connectionLog.get(connectionLog.size() - 1)));
                    }
                    sendAndReceive();
                    //System.out.println(methods.tuple(isReceiving(), in.canRun(), messageProcessed, this));
                }
                //System.out.println(methods.tuple(101, this));
                endThread("run()");
            }
        }
        //System.out.println(methods.tuple("INTERRUPT_THREAD", this));
        getThread().interrupt();
    }

    public void endThread(Exception e, String reason) {
        in.endThread();
        super.endThread(e, reason);
    }
}
