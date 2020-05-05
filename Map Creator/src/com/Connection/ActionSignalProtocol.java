package com.Connection;

import com.Connection.Hosts.ServerHost;
import com.GUI.GUI;

public class ActionSignalProtocol implements Runnable {
    private ActionCommand command;
    private ServerHost server;
    private GUI frame;
    private HostThread thread = new HostThread(this);
    public ActionSignalProtocol(ServerHost server) {
        this.server = server;
        thread.start();
    }

    public boolean canRun() {
        if (thread != null) {
            if (thread.running) {
                return true;
            }
        }
        return false;
    }

    public HostThread getThread() {
        return thread;
    }

    public void takeCommand() {
        if (!server.queue.isEmpty()) {
            if (!holdingCommand()) {
                setCommand(server.queue.remove());
            }
        }
    }
    public void setCommand(ActionCommand command) {
        this.command = command;
        System.out.println(command.command);
        releaseCommand();
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
        server.sendToThread(command);
        setCommand();
    }

    public void run() {
        while (canRun()) {
            takeCommand();
        }
        thread.interrupt();
    }
}
