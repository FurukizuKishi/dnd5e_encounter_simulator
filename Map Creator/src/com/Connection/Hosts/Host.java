package com.Connection.Hosts;

import java.util.ArrayList;

public class Host {
    public String hostName;
    public int portNumber;
    public ArrayList<String> connectionLog = new ArrayList<>();

    public void addLog(String message) {
        connectionLog.add(message);
    }
}
