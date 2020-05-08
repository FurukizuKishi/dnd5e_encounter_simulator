package com.Connection.Hosts;

import com.Connection.CreateSessionGUI;
import com.Connection.JoinSessionGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ServerThreadHost extends SingleHost {
    private ServerHost server;
    boolean sentNameRequest = false;
    public ServerThreadHost(CreateSessionGUI frame, ServerHost server, Socket socket) {
        this.frame = frame;
        this.server = server;
        if (connect(socket)) {
            logList = frame.addConnection(frame.w, frame.h, this);
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
    }

    public void closeThread() {
        addLog(out, "Connection closed.");
        server.endClient(this);
    }

    public String receiveMessage() {
        String message = super.receiveMessage();
        server.addCommand(this, message);
        System.out.println(server.queue.size() + " command(s), message '" + message + "'");
        return message;
    }

    public boolean sendAndReceive() {
        if (!sentNameRequest) {
            sendMessage("Who are you? I need your name.");
            sentNameRequest = true;
        }
        String message = receiveMessage();
        if (message != null) {
            if (disconnect(message)) {
                return false;
            }
            if (message.contains("Hello, I am ")) {
                String name = message.substring("Hello, I am ".length(), message.length() - 1);
                ((CreateSessionGUI) frame).renameConnectionLog(this, name);
                sendMessage("Very well. " + name + ", welcome to the game.");
            }
        }
        else {
            if (!timedown()) {
                return false;
            }
        }
        if (holdingMessage()) {
            sendMessage(this.message);
        }
        return true;
    }
}
