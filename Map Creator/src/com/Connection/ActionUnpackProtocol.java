package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.GUI.GUI;
import com.System.Die;
import com.methods;

public class ActionUnpackProtocol {
    private String command = null;
    private ClientHost client;
    public ActionUnpackProtocol(ClientHost client) {
        this.client = client;
    }
    public boolean setCommand(String command) {
        if (command != null) {
            System.out.println(methods.tuple("SET", this, command));
            this.command = command;
            processCommand();
            releaseCommand();
            return true;
        }
        return false;
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
                Integer roll = Integer.parseInt(diceRoll[1]);
                client.addLog(args[0] + " = " + roll);
                break;
            case "MOVE":

                break;
            case "CHAR":
                client.getGame().players.get(args[0]).charSheet.getSheetComponents().updateStat(args[1], Integer.parseInt(args[2]));
                client.addLog("Set " + args[0] + "'s " + args[1].toUpperCase() + " to " + args[2] + ".");
                break;
        }
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
        client.sentFlag = false;
        setCommand();
    }
}
