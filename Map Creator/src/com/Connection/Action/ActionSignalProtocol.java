package com.Connection.Action;

import com.Connection.Hosts.ServerHost;
import com.Game.System.Die;
import com.Game.methods;

public class ActionSignalProtocol extends ActionProtocol<ActionCommand> {
    private ServerHost server;
    public ActionSignalProtocol(ServerHost server) {
        this.server = server;
        host = server;
        startThread();
    }

    public void continueTakeProcess() {
        processCommand();
        releaseCommand();
    }

    public boolean setItem(ActionCommand command) {
        if (command != null) {
            if (command.command != null) {
                if (methods.messageIsFlag(command.command)) {
                    super.setItem(command);
                    return true;
                }
            }
        }
        this.command = command;
        return false;
    }

    public void releaseCommand() {
        server.sendToThread(command);
        super.releaseCommand();
    }

    public void processCommand() {
        String[] unpacked = command.command.split(":");
        if (command.recipients == null) {
            command.recipients = unpacked[0].split(",");
        }
        String flag = unpacked[1];
        String[] args = unpacked[2].split(",");
        String[] result = null;
        switch (flag) {
            case "ROLL":
                result = new String[] {flag, args[0], null};
                //System.out.println(args[0]);
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
                result = new String[] {flag, args[0] + "," + args[1] + "," + args[2]};
                break;
            case "CHAR":
                result = new String[] {flag, args[0] + "," + args[1] + "," + args[2]};
                break;
        }
        if (result != null) {
            command.result = methods.concencateList(result, ":");
            //System.out.println(methods.tuple("PROCESS", this, command.host, command.result));
        }
    }
}
