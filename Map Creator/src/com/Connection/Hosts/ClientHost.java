package com.Connection.Hosts;

import com.Connection.CreateSessionGUI;
import com.Connection.JoinSessionGUI;
import com.methods;
import com.swingMethods;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHost extends SingleHost {
    String prevMessage = null;
    boolean sentName = false;
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

    public void endThread(Exception e) {
        super.endThread(e);
        try {
            socket.close();
            stdIn.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        frame.closeConnectionLog(frame.w, frame.h);
        frame.changeConnectionButtons(false);
    }

    public void sendMessage(String message) {
        prevMessage = this.message;
        super.sendMessage(message);
    }
    public boolean sendAndReceive() {
        String message = receiveMessage();
        System.out.println(message);
        if (message == null) {
            if (!timedown()) {
                return false;
            }
        }
        else {
            if (!sentName) {
                if (message.equals("Who are you? I need your name.")) {
                    sendMessage("Hello, I am " + name + ".");
                    sentName = true;
                }
            }
            if (disconnect(message)) {
                return false;
            }
            if (holdingMessage()) {
                sendMessage(createFlag());
            }
        }
        return true;
    }

    public String createFlag() {
        String[] flags = {"ROLL", "MOVE", "CHAR"};
        int[] die = {2, 4, 6, 8, 10, 12, 20, 100};
        String[] stats = {"hp", "str", "dex", "con", "int", "wis", "cha"};
        int n = (int) (Math.random() * flags.length);
        switch (flags[n]) {
            case "ROLL":
                int number = (int) (Math.random() * 10);
                int sides = (int) (Math.random() * die.length);
                return "ROLL:" + number + "d" + die[sides];
            case "MOVE":
                int x = (int) (Math.random() * 10);
                int y = (int) (Math.random() * 10);
                return "MOVE:" + x + "," + y;
            case "CHAR":
                int stat = (int) (Math.random() * 10);
                int value = (int) (Math.random() * 12) + 8;
                if (stats[stat].equals("hp")) {
                    value = (int) (Math.random() * 92) + 8;
                }
                return "CHAR:" + stats[stat] + "," + value;
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
