package com.Connection.Hosts;

import com.Connection.Action.ActionUnpackProtocol;
import com.Connection.GUI.JoinSessionGUI;
import com.Game.Entities.Characters.CharacterModel;
import com.Game.GUI.GUI;
import com.Game.main;
import com.Game.methods;
import com.Game.swingMethods;

import java.io.IOException;
import java.net.Socket;

public class ClientHost extends SingleHost {
    private ActionUnpackProtocol protocol;
    private boolean sentName = false;
    private boolean sentMaster = false;
    private boolean receivedNameConfirmation = false;
    GUI game;
    private boolean master;
    public boolean sentFlag = false;
    public ClientHost(JoinSessionGUI frame, boolean master) {
        this.frame = frame;
        this.master = master;
        logList = frame.connectionLog;
        name = Integer.toString(hashCode());
        frame.clientName = name;
        frame.connectionScrollbar.setBorder(swingMethods.createBorder(name));
        game = main.main2(this);
    }
    public ClientHost(JoinSessionGUI frame, Socket socket, boolean master) {
        this(frame, master);
        connect(socket);
    }
    public ClientHost(JoinSessionGUI frame, String hostName, int portNumber, boolean master) {
        this(frame, master);
        if (connect(hostName, portNumber)) {
            getThread().start();
        }
    }
    public ClientHost(JoinSessionGUI frame) {
        this(frame, false);
    }
    public ClientHost(JoinSessionGUI frame, Socket socket) {
        this(frame, socket, false);
    }
    public ClientHost(JoinSessionGUI frame, String hostName, int portNumber) {
        this(frame, hostName, portNumber, false);
    }

    public GUI getGame() {
        return game;
    }

    public void startThread() {
        super.startThread();
        createProtocol();
    }
    public void endThread(Exception e, String reason) {
        super.endThread(e, reason);
        try {
            socket.close();
            stdIn.close();
            destroyProtocol();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        frame.closeConnectionLog(frame.w, frame.h);
        frame.changeConnectionButtons(false);
    }

    public boolean createProtocol() {
        if (protocol == null) {
            protocol = new ActionUnpackProtocol(this);
            return true;
        }
        return false;
    }
    public boolean destroyProtocol() {
        if (protocol != null) {
            protocol = null;
            return true;
        }
        return false;
    }

    public void setFlag(String flag, CharacterModel character, String stat, int value) {
        switch (flag) {
            case "CHAR":
                setMessage(("ALL(" + name + "):" + flag + ":" + character.name + "," + stat + "," + value));
                break;
        }
    }
    public boolean holdingFlag() {
        if (holdingMessage()) {
            if (methods.messageIsFlag(message)) {
                return true;
            }
        }
        return false;
    }
    public void sendMessage(String message) {
        this.message = message;
        super.sendMessage(message);
    }
    public boolean sendAndReceive() {
        //System.out.println(methods.tuple("holding", message, messageProcessed, this));
        if (disconnect(message)) {
            return false;
        }
        if (receivedNameConfirmation) {
            if (holdingFlag() && !sentFlag) {
                sendMessage(this.message);
                sentFlag = true;
            }
        }
        if (holdingMessage() && !messageProcessed) {
            String message = this.message;
            System.out.println(message);
            if (!receivedNameConfirmation || holdingFlag() || message.startsWith("Processed command")) {
                System.out.println(methods.tuple("MSG", message, this));
                if (!messageProcessed) {
                    try {
                        String substring = message.substring("Processed command \"".length(), message.length() - 2);
                        if (methods.messageIsFlag(substring)) {
                            createProtocol();
                            addCommand(substring);
                            addLog("Unpacked command \"" + substring + "\".");
                            System.out.println(methods.tuple("MSG_FLAG", substring, protocol));
                            setMessage();
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(methods.tuple("MSG_FLAG", null));
                    }
                    if (message.equals("Who are you? I need your name.") && !sentName) {
                        sendMessage("Hello, I am " + name + ".");
                        sentName = true;
                    } else if (message.equals("Very well. " + name + ", welcome to the game.") && (master && !sentMaster)) {
                        sendMessage("I am the dungeon master's client.");
                        sentMaster = true;
                    } else if (!receivedNameConfirmation) {
                        if ((!master && sentName) || (master && sentMaster)) {
                            receivedNameConfirmation = true;
                        }
                    }
                }
            }
            messageProcessed = true;
        }
        return true;
    }

    public String createFlag() {
        String[] flags = {"ROLL", "MOVE", "CHAR"};
        int[] die = {2, 4, 6, 8, 10, 12, 20, 100};
        String[] stats = {"hp", "str", "dex", "con", "int", "wis", "cha"};
        int n = (int) (Math.random() * flags.length);
        String prefix = name + ":" + flags[n] + ":";
        switch (flags[n]) {
            case "ROLL":
                int number = (int) (Math.random() * 9) + 1;
                int sides = (int) (Math.random() * die.length);
                return prefix + number + "d" + die[sides];
            case "MOVE":
                int x = (int) (Math.random() * 10);
                int y = (int) (Math.random() * 10);
                return prefix + x + "," + y;
            case "CHAR":
                int stat = (int) (Math.random() * stats.length);
                int value = (int) (Math.random() * 12) + 8;
                if (stats[stat].equals("hp")) {
                    value = (int) (Math.random() * 92) + 8;
                }
                return prefix + stats[stat] + "," + value;
        }
        return null;
    }
}
