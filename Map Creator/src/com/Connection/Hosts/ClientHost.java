package com.Connection.Hosts;

import com.Connection.ActionUnpackProtocol;
import com.Connection.JoinSessionGUI;
import com.methods;
import com.swingMethods;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHost extends SingleHost {
    String prevMessage = null;
    private ActionUnpackProtocol protocol;
    boolean sentName = false;
    boolean receivedNameConfirmation = false;
    String name;
    public ClientHost(JoinSessionGUI frame) {
        this.frame = frame;
        logList = frame.connectionLog;
        name = Integer.toString(hashCode());
        frame.clientName = name;
        frame.connectionScrollbar.setBorder(swingMethods.createBorder(name));
    }
    public ClientHost(JoinSessionGUI frame, Socket socket) {
        this(frame);
        connect(socket);
    }
    public ClientHost(JoinSessionGUI frame, String hostName, int portNumber) {
        this(frame);
        if (connect(hostName, portNumber)) {
            getThread().start();
        }
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
        String message = receiveMessage();
        System.out.println(methods.tuple("holding", this.message, "received", message));
        if (message == null) {
            if (!holdingMessage()) {
                if (!timedown()) {
                    return false;
                }
            }
        }
        else {
            if (methods.messageIsFlag(message)) {
                if (protocol == null) {
                    createProtocol();
                }
                protocol.setCommand(message);
            }
            if (!sentName) {
                if (message.equals("Who are you? I need your name.")) {
                    sendMessage("Hello, I am " + name + ".");
                    sentName = true;
                }
            }
            else if (!receivedNameConfirmation) {
                if (message.equals("Very well. " + name + ", welcome to the game.")) {
                    receivedNameConfirmation = true;
                }
            }
            if (disconnect(message)) {
                return false;
            }
            if (!holdingFlag() && receivedNameConfirmation) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    sendMessage(createFlag());
                }
                catch (InterruptedException e) {
                    addLog("[ERR]: " + e);
                    return false;
                }
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
