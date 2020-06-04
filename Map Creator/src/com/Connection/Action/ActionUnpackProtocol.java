package com.Connection.Action;

import com.Connection.Hosts.ClientHost;
import com.Game.Entities.Characters.CharacterModel;
import com.Game.methods;

import java.util.ArrayList;

public class ActionUnpackProtocol extends ActionProtocol<String> {
    private ClientHost client;
    public ActionUnpackProtocol(ClientHost client) {
        this.client = client;
        host = client;
        startThread();
    }

    public void continueTakeProcess() {
        processCommand();
        releaseCommand();
    }

    public boolean setItem(String command) {
        if (command != null) {
            client.addLog("Set the command \"" + command + "\".");
            this.command = command;
            return true;
        }
        return false;
    }

    public void releaseCommand() {
        client.addLog("Released the command \"" + command + "\".");
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
                ArrayList<int[]> pathList = new ArrayList<>();
                String str = "";
                boolean inNode = false;
                int[] node;
                for (int i = 0; i < unpacked[1].length(); i += 1) {
                    if (unpacked[1].charAt(i) == ']') {
                        String[] nodeString = str.split(",");
                        node = new int[] { Integer.parseInt(nodeString[0]), Integer.parseInt(nodeString[1]) };
                        pathList.add(node);
                        inNode = false;
                        str = "";
                    }
                    if (inNode) {
                        str += unpacked[1].charAt(i);
                    }
                    if (unpacked[1].charAt(i) == '[') {
                        inNode = true;
                    }
                }
                CharacterModel character = client.getGame().players.get(args[0]);
                character.actor.move(pathList);
                break;
            case "CHAR":
                client.getGame().players.get(args[0]).charSheet.updateStat(args[1], Integer.parseInt(args[2]));
                client.addLog("Set " + args[0] + "'s " + args[1].toUpperCase() + " to " + args[2] + ".");
                break;
        }
    }
}
