package com.Connection.Hosts;

import com.Connection.ActionUnpackProtocol;
import com.Connection.JoinSessionGUI;
import com.Entities.Characters.CharacterModel;
import com.GUI.GUI;
import com.main;
import com.methods;
import com.swingMethods;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHost extends SingleHost {
    private ActionUnpackProtocol protocol;
    boolean sentName = false;
    boolean receivedNameConfirmation = false;
    public String name;
    GUI game;
    boolean master;
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
        if (message != null) {
            System.out.println(methods.tuple("holding", message));
        }
        if (receivedNameConfirmation) {
            if (holdingFlag() && !sentFlag) {
                sendMessage(this.message);
                sentFlag = true;
                /*try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                catch (InterruptedException e) {
                    addLog("[ERR]: " + e);
                    return false;
                }*/
            }
        }
        if (!receivedNameConfirmation || holdingFlag()) {
            System.out.println(70);
            String message = receiveMessage();
            System.out.println(methods.tuple("received", message));
            if (message == null) {
                if (!holdingMessage()) {
                    if (!timedown()) {
                        return false;
                    }
                }
            } else {
                try {
                    String message2 = message.substring("Processed command \"".length(), message.length() - 2);
                    System.out.println(methods.tuple("PROCESSED_COMMAND", message2));
                    if (methods.messageIsFlag(message2)) {
                        if (protocol == null) {
                            createProtocol();
                        }
                        protocol.setCommand(message2);
                        addLog("Unpacked command \"" + message2 + "\".");
                        setMessage();
                    }
                }
                catch (IndexOutOfBoundsException e) {

                }
                System.out.println(methods.tuple("SKIP_COMMAND"));
                if (!sentName) {
                    if (message.equals("Who are you? I need your name.")) {
                        sendMessage("Hello, I am " + name + ".");
                        if (master) {
                            sendMessage("I am the dungeon master's client.");
                        }
                        sentName = true;
                    }
                } else if (!receivedNameConfirmation) {
                    if ((!master && message.equals("Very well. " + name + ", welcome to the game.")) || (master && message.equals("Very well. " + name + ", I have assigned you to the dungeon master."))) {
                        receivedNameConfirmation = true;
                    }
                }
                if (disconnect(message)) {
                    return false;
                }
            /*if (!holdingFlag() && receivedNameConfirmation) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    sendMessage(createFlag());
                }
                catch (InterruptedException e) {
                    addLog("[ERR]: " + e);
                    return false;
                }
            }*/
            }
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

    /*public void run() {
        while (canRun()) {
            if (connectionLog != null) {
                int i = 0;
                while (in != null) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        i = sendMessage(i);
                    }
                    catch (InterruptedException e) {
                        sendMessage("[ERR]: " + e.getMessage());
                    }
                }
                endThread();
            }
        }
        getThread().interrupt();
    }*/
}
