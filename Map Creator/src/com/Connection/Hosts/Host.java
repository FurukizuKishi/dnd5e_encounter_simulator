package com.Connection.Hosts;

import com.Connection.ConnectionGUI;
import com.Connection.HostThread;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Host implements Runnable {
    public ConnectionGUI frame;
    public String hostName;
    public int portNumber;
    public ArrayList<String> connectionLog = new ArrayList<>();
    public JList logList;
    protected PrintWriter out;
    protected BufferedReader in;
    protected BufferedReader stdIn;
    private HostThread thread = new HostThread(this);

    public void addLog(String message) {
        addLog(null, message);
    }
    public void addLog(PrintWriter out, String message) {
        if (out != null) {
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

    public HostThread getThread() {
        return thread;
    }

    public boolean canRun() {
        if (thread != null) {
            if (thread.running) {
                return true;
            }
        }
        return false;
    }

    public void startThread() {
        thread.start();
    }
    public void endThread() {
        endThread(null);
    }
    public void endThread(Exception e) {
        thread.end();
        if (e != null) {
            String message = "[ERR/" + e.getCause().toString() + "]: " + e.getMessage();
            System.out.println(message);
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

    public void run() {
        while (canRun()) {
            if (frame.connectionLog != null) {
                try {
                    String input;
                    while (in != null) {
                        if ((input = in.readLine()) != null) {
                            addLog(input);
                            if (input.contains("ERR")) {
                                break;
                            }
                            else {
                                addLog(out, "Hello");
                            }
                            /*
                            String cInput = stdIn.readLine();
                            if (cInput != null) {
                                System.out.println("Client: " + cInput);
                            }*/
                        }
                    }
                    endThread();
                } catch (Exception e) {
                    endThread(e);
                }
            }
        }
        getThread().interrupt();
    }
}
