package com.Connection;

import com.Connection.Hosts.ServerHost;
import com.GUI.GUI;
import com.methods;

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
        if (!holdingCommand()) {
            if (!server.queue.isEmpty()) {
                setCommand(server.queue.remove());
            }
        }
    }
    public void setCommand(ActionCommand command) {
        System.out.println(methods.tuple(this, "SET", command.command));
        this.command = command;
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
        System.out.println(methods.tuple(this, "RELEASE", command.command));
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
