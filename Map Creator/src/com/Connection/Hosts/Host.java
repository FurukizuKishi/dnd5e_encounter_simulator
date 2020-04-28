package com.Connection.Hosts;

import com.Connection.ConnectionGUI;

import java.util.ArrayList;

public class Host {
    public ConnectionGUI frame;
    public String hostName;
    public int portNumber;
    public ArrayList<String> connectionLog = new ArrayList<>();

    public void addLog(String message) {
        connectionLog.add(message);
    }
}
