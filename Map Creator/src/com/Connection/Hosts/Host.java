package com.Connection.Hosts;

import com.Connection.ConnectionGUI;
import com.Connection.HostRunnable;
import com.Connection.HostThread;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Host extends HostRunnable {
    public ConnectionGUI frame;
    public String hostName;
    public int portNumber;
    public ArrayList<String> connectionLog = new ArrayList<>();
    public JList logList;
    protected PrintWriter out;
    protected BufferedReader in;
    protected BufferedReader stdIn;

    public void addLog(String message) {
        addLog(null, message);
    }
    public void addLog(PrintWriter out, String message) {
        if (out != null) {
            out.flush();
            out.println(message);
            connectionLog.add("Me: " + message);
        }
        else {
            connectionLog.add("Remote: " + message);
        }
        if (logList != null) {
            logList.setListData(connectionLog.toArray());
        }
        frame.repaint();
    }

    public void endThread(String reason) {
        endThread(null, reason);
    }
    public void endThread(Exception e, String reason) {
        super.endThread(e, reason);
        if (e != null) {
            String message = e.getMessage() + ".";
            System.out.println(methods.tuple("ERR", this, message, reason));
            addLog(out, "[ERR]: " + message);
        }
        else {
            String message = "Connection closed gracefully.";
            System.out.println(methods.tuple("DBG", this, message, reason));
            addLog(out, message);
        }
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            String input;
            addLog(out, message);
            /*if ((input = in.readLine()) != null) {
                addLog(input);
                if (input.contains("ERR")) {
                    addLog(out, "Error Received.");
                }
            }*/
        }
        catch (Exception e) {
            endThread(e, "sendMessage(\"" + message + "\")");
        }
    }
    public int sendMessage(int i) {
        sendMessage(Integer.toString(i));
        return i + 1;
    }

    public String receiveMessage() {
        try {
            String input, message;
            if ((input = in.readLine()) != null) {
                addLog(input);
                message = input;
                if (input.contains("ERR")) {
                    message = "Error Received.";
                    addLog(out, message);
                }
                return message;
            }
        }
        catch (Exception e) {
            endThread(e, "receiveMessage()");
        }
        return null;
    }

    @Override
    public void run() {
        while (canRun()) {
            if (connectionLog != null) {
                while (in != null) {
                    receiveMessage();
                }
                endThread("run()");
            }
        }
        getThread().interrupt();
    }
}
