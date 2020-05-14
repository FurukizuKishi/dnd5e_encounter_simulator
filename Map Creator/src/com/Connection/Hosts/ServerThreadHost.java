package com.Connection.Hosts;

import com.Connection.GUI.CreateSessionGUI;
import com.Game.methods;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ServerThreadHost extends SingleHost {
    private ServerHost server;
    public int pos;
    boolean sentNameRequest = false;
    public boolean nameAssigned = false;
    public ServerThreadHost(CreateSessionGUI frame, ServerHost server, Socket socket) {
        this.frame = frame;
        this.server = server;
        pos = server.getClients().size() + 1;
        //System.out.println(methods.tuple("POS", pos));
        if (connect(socket)) {
            logList = frame.addConnection(frame.w, frame.h, this);
            getThread().start();
        }
    }

    public void endThread(Exception e, String reason) {
        super.endThread(e, reason);
        try {
            socket.close();
            stdIn.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeThread() {
        addLog(out, "Connection closed.");
        server.endClient(this);
    }

    public String receiveMessage(String message) {
        super.receiveMessage(message);
        //System.out.println(server.queue.size() + " command(s), message \"" + message + "\"");
        if (message != null) {
            //System.out.println(methods.messageIsFlag(message));
            if (methods.messageIsFlag(message)) {
                server.addCommand(this, message);
            }
        }
        return message;
    }

    public boolean sendAndReceive() {
        //System.out.println(methods.tuple("holding", message, messageProcessed, this));
        if (disconnect(message)) {
            return false;
        }
        if (!sentNameRequest) {
            sendMessage("Who are you? I need your name.");
            sentNameRequest = true;
        }
        if (holdingMessage() && !messageProcessed) {
            //System.out.println(methods.tuple("MSG", message, this));
            if (message.contains("Hello, I am ") && !nameAssigned) {
                String name = message.substring("Hello, I am ".length(), message.length() - 1);
                ((CreateSessionGUI) frame).renameConnectionLog(this, name);
                sendMessage("Very well. " + name + ", welcome to the game.");
                nameAssigned = true;
            }
            if (message.contains("I am the dungeon master's client.") && name != null) {
                ((CreateSessionGUI) frame).renameConnectionLog(this, name);
                sendMessage("Very well. " + name + ", I have assigned you to the dungeon master.");
                server.master = this;
            }
            if (methods.messageIsFlag(message)) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                catch (InterruptedException e) {
                    endThread(e, "sendAndReceive()");
                }
            }
            messageProcessed = true;
        }
        return true;
    }
}