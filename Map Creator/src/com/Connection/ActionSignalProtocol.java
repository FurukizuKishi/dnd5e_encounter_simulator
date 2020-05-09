package com.Connection;

import com.Connection.Hosts.ServerHost;
import com.GUI.GUI;
import com.System.Die;
import com.methods;

public class ActionSignalProtocol extends HostRunnable {
    private ActionCommand command = null;
    private ServerHost server;
    private GUI frame;
    public ActionSignalProtocol(ServerHost server) {
        this.server = server;
        startThread();
    }

    public boolean takeCommand() {
        if (!holdingCommand()) {
            if (!server.queue.isEmpty()) {
                ActionCommand command = server.queue.remove();
                if (command != null) {
                    if (!setCommand(command)) {
                        server.endThread("takeCommand(" + command.command + ")");
                        return false;
                    }
                }
                processCommand();
                releaseCommand();
            }
        }
        return true;
    }
    public boolean setCommand(ActionCommand command) {
        if (command != null) {
            if (command.command != null) {
                if (methods.messageIsFlag(command.command)) {
                    System.out.println(methods.tuple("SET", this, command.host, command.command));
                    this.command = command;
                    return true;
                }
            }
        }
        this.command = command;
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
        System.out.println(methods.tuple("RELEASE", this, command.host, command.command));
        server.sendToThread(command);
        setCommand();
    }

    public void processCommand() {
        String[] unpacked = command.command.split(":");
        command.recipients = unpacked[0].split(",");
        String flag = unpacked[1];
        String[] args = unpacked[2].split(",");
        String[] result = null;
        switch (flag) {
            case "ROLL":
                result = new String[] {flag, args[0], null};
                System.out.println(args[0]);
                Die die = new Die(args[0]);
                int[] roll = die.rollOut();
                String summation = "" + roll[1];
                for (int i = 2; i < roll.length; i += 1) {
                    if (roll[i] < 0) {
                        summation += "-" + roll[i];
                    }
                    else {
                        summation += "+" + roll[i];
                    }
                }
                result[2] = summation + "=" + roll[0];
                break;
            case "MOVE":
                result = new String[] {flag, args[0] + "," + args[1]};
                break;
            case "CHAR":
                result = new String[] {flag, args[0] + "," + args[1]};
        }
        if (result != null) {
            command.result = methods.concencateList(result, ":");
            System.out.println(methods.tuple("PROCESS", this, command.host, command.result));
        }
    }

    public void run() {
        while (canRun()) {
            takeCommand();
        }
        getThread().interrupt();
    }
}
