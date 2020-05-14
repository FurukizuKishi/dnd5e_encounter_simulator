package com.Connection.Action;

import com.Connection.Hosts.ClientHost;
import com.Game.methods;

public class ActionUnpackProtocol extends ActionProtocol<String> {
    private ClientHost client;
    public ActionUnpackProtocol(ClientHost client) {
        this.client = client;
        host = client;
        startThread();
    }

    public boolean setItem(String command) {
        if (command != null) {
            this.command = command;
            return true;
        }
        return false;
    }

    public void releaseCommand() {
        client.setMessage();
        client.sentFlag = false;
        super.releaseCommand();
    }

    public void processCommand() {
        String[] unpacked = command.split(":");
        String flag = unpacked[0];
        String[] args = unpacked[1].split(",");
        String[] result = null;
        switch (flag) {
            case "ROLL":
                String[] diceRoll = args[1].split("=");
                String[] rollOut = diceRoll[0].split("\\+");
                int roll = Integer.parseInt(diceRoll[1]);
                client.addLog(args[0] + " = " + roll);
                break;
            case "MOVE":

                break;
            case "CHAR":
                client.getGame().players.get(args[0]).charSheet.updateStat(args[1], Integer.parseInt(args[2]));
                client.addLog("Set " + args[0] + "'s " + args[1].toUpperCase() + " to " + args[2] + ".");
                break;
        }
    }
}
