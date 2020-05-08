package com.Connection;

import com.Connection.Hosts.ServerHost;
import com.GUI.GUI;
import com.methods;

public class ActionSignalProtocol extends HostRunnable {
    private ActionCommand command = null;
    private ServerHost server;
    private GUI frame;
    public ActionSignalProtocol(ServerHost server) {
        this.server = server;
        startThread();
    }

    public void takeCommand() {
        if (!holdingCommand()) {
            if (!server.queue.isEmpty()) {
                if (!setCommand(server.queue.remove())) {
                    server.endThread();
                }
            }
        }
    }
    public boolean setCommand(ActionCommand command) {
        if (command != null) {
            if (command.command != null) {
                System.out.println(methods.tuple("SET", command.host, command.command));
                this.command = command;
                releaseCommand();
                return true;
            }
        }
        return false;
    }
    public void setCommand() {
        setCommand(null);
    }
    public ActionCommand getCommand() {
        return command;
    }
    public boolean holdingCommand() {
        return (command != null);
    }
    public void releaseCommand() {
        System.out.println(methods.tuple("RELEASE", command.host, command.command));
        server.sendToThread(command);
        setCommand();
    }

    public void run() {
        while (canRun()) {
            takeCommand();
        }
        getThread().interrupt();
    }
}
