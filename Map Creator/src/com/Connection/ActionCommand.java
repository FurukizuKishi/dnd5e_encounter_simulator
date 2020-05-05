package com.Connection;

import com.Connection.Hosts.ServerThreadHost;

public class ActionCommand {
    public String command;
    public ServerThreadHost host;

    public ActionCommand(ServerThreadHost host, String command) {
        this.host = host;
        this.command = command;
    }
}
