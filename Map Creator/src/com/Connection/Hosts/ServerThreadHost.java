package com.Connection.Hosts;

import com.Connection.CreateSessionGUI;
import com.Connection.JoinSessionGUI;
import com.methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class ServerThreadHost extends SingleHost {
    private ServerHost server;
    public String name;
    boolean sentNameRequest = false;
    public ServerThreadHost(CreateSessionGUI frame, ServerHost server, Socket socket) {
        this.frame = frame;
        this.server = server;
        name = Integer.toString(server.getClients().size() + 1);
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

    public String receiveMessage() {
        String message = super.receiveMessage();
        System.out.println(server.queue.size() + " command(s), message \"" + message + "\"");
        if (message != null) {
            System.out.println(methods.messageIsFlag(message));
            if (methods.messageIsFlag(message)) {
                server.addCommand(this, message);
            }
        }
        return message;
    }

    public boolean sendAndReceive() {
        if (!sentNameRequest) {
            sendMessage("Who are you? I need your name.");
            sentNameRequest = true;
        }
        if (holdingMessage()) {
            System.out.println(60 + " " + this.message);
            sendMessage(this.message);
        }
        String message = receiveMessage();
        if (message != null) {
            System.out.println(61 + " " + message);
            if (disconnect(message)) {
                return false;
            }
            if (message.contains("Hello, I am ")) {
                String name = message.substring("Hello, I am ".length(), message.length() - 1);
                ((CreateSessionGUI) frame).renameConnectionLog(this, name);
                sendMessage("Very well. " + name + ", welcome to the game.");
            }
            if (methods.messageIsFlag(message)) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                catch (InterruptedException e) {
                    endThread(e, "sendAndReceive()");
                }
            }
        } else {
            System.out.println(62);
            if (!timedown()) {
                return false;
            }
        }
        return true;
    }
}