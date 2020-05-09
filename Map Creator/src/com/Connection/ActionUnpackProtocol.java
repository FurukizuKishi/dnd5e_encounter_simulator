package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.GUI.GUI;
import com.methods;

public class ActionUnpackProtocol {
    private String command = null;
    private ClientHost client;
    private GUI frame;
    public ActionUnpackProtocol(ClientHost client) {
        this.client = client;
    }
    public boolean setCommand(String command) {
        if (command != null) {
            System.out.println(methods.tuple("SET", this, command));
            this.command = command;
            releaseCommand();
            return true;
        }
        return false;
    }
    public void setCommand() {
        setCommand(null);
    }
    public String getCommand() {
        return command;
    }
    public boolean holdingCommand() {
        return (command != null);
    }
    public void releaseCommand() {
        System.out.println(methods.tuple("RELEASE", this, command));
        client.setMessage();
        setCommand();
    }
}
