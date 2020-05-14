package com.Connection.Hosts;

import com.Connection.GUI.ConnectionGUI;
import com.Connection.Threads.HostRunnable;
import com.Game.methods;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Host<T> extends HostRunnable {
    public ConnectionGUI frame;
    public String hostName;
    public int portNumber;
    public ArrayList<String> connectionLog = new ArrayList<>();
    public Queue<T> queue = new LinkedList<>();
    public JList logList;
    protected PrintWriter out;
    protected BufferedReader in = null;

    public void addLog(String message) {
        addLog(null, message);
    }
    public void addLog(PrintWriter out, String message) {
        if (out != null) {
            connectionLog.add("Me: " + message);
        }
        else {
            connectionLog.add("Remote: " + message);
        }
        if (logList != null) {
            logList.setListData(connectionLog.toArray());
        }
        frame.repaint();
        if (out != null) {
            out.flush();
            out.println(message);
        }
    }

    public boolean isReceiving() {
        return (getInput() != null);
    }
    public BufferedReader getInput() {
        return in;
    }
    public abstract void setInput(BufferedReader input);

    public void closeThread() {
        addLog(out, "Connection closed.");
        endThread("closeThread()");
    }

    public void endThread(String reason) {
        endThread(null, reason);
    }
    public void endThread(Exception e, String reason) {
        super.endThread(e, reason);
        if (e != null) {
            String message = e.getMessage() + ".";
            //System.out.println(methods.tuple("ERR", this.toString(), message, reason));
            addLog(out, "Connection closed with an error: " + message);
        }
        else {
            String message = "Connection closed gracefully.";
            //System.out.println(methods.tuple("DBG", this.toString(), message, reason));
            addLog(out, message);
        }
        if (out != null) {
            out.close();
            out = null;
        }
    }

    public void addCommand(T command) {
        queue.add(command);
    }

    public void sendMessage(String message) {
        try {
            addLog(out, message);
        }
        catch (Exception e) {
            endThread(e, "sendMessage(\"" + message + "\")");
        }
    }

    public String receiveMessage() {
        return null;
    }

    @Override
    public void run() {
        while (canRun()) {
            if (connectionLog != null) {
                while (isReceiving()) {
                    receiveMessage();
                }
                endThread("run()");
            }
        }
        getThread().interrupt();
    }
}
