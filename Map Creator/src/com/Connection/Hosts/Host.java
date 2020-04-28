package com.Connection.Hosts;

import com.Connection.ConnectionGUI;
import com.Connection.HostThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Host implements Runnable {
    public ConnectionGUI frame;
    public String hostName;
    public int portNumber;
    public ArrayList<String> connectionLog = new ArrayList<>();
    protected PrintWriter out;
    protected BufferedReader in;
    protected BufferedReader stdIn;
    private HostThread thread = new HostThread(this);

    public void addLog(PrintWriter out, String message) {
        out.println(message);
        connectionLog.add(message);
        if (frame.connectionLog != null) {
            frame.connectionLog.setListData(connectionLog.toArray());
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
            System.out.println(e);
            e.printStackTrace();
        }
        try {
            out.close();
            out = null;
            in.close();
            in = null;
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
                            System.out.println("Server: " + input);
                            addLog(out, input);

                            String cInput = stdIn.readLine();
                            if (cInput != null) {
                                System.out.println("Client: " + cInput);
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
