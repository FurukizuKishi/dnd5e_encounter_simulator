package com.Connection.Hosts;

import com.Connection.ConnectionGUI;
import com.Connection.HostThread;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
            String message = "[ERR]: " + e.getMessage();
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

    public int sendMessage(int i) {
        try {
            String input;
            //TimeUnit.SECONDS.sleep(3);
            addLog(out, Integer.toString(i));
            /*Object[] stream = in.lines().toArray();
            if (stream.length > 0) {
                if ((input = (String) stream[stream.length - 1]) != null) {
                    addLog(input);
                    if (input.contains("ERR")) {
                        break;
                    }
                }
            }*/
            if ((input = in.readLine()) != null) {
                addLog(input);
                if (input.contains("ERR")) {
                    addLog(out, "Error Received.");
                }
            }
            return i + 1;
        }
        catch (Exception e) {
            endThread(e);
        }
        return -1;
    }

    public void recieveMessage() {
        try {
            String input;
            /*Object[] stream = in.lines().toArray();
            addLog(out, methods.tuple(stream));
            if (stream.length > 0) {
                if ((input = (String) stream[stream.length - 1]) != null) {
                    addLog(input);
                    if (input.contains("ERR")) {
                        break;
                    }
                    else {
                        addLog(out, "Hello. You gave me an input of '" + input + "'.");
                    }
                }
            }*/
            /*if (in.lines() != null) {
                Object[] stream = in.lines().toArray();
                addLog("[IN]: " + stream.length);
            }*/
            if ((input = in.readLine()) != null) {
                addLog(input);
                if (input.contains("ERR")) {
                    addLog(out, "Error Received.");
                } else {
                    addLog(out, "Hello. You gave me an input of '" + input + "'.");
                }
            }
        }
        catch (Exception e) {
            endThread(e);
        }
    }

    public void run() {
        while (canRun()) {
            if (connectionLog != null) {
                while (in != null) {
                    recieveMessage();
                }
                endThread();
            }
        }
        getThread().interrupt();
    }
}
