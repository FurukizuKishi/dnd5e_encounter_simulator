package com.Connection.Hosts;

import com.Connection.CreateSessionGUI;
import com.Connection.JoinSessionGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadHost extends SingleHost {
    private ServerHost server;
    public ServerThreadHost(CreateSessionGUI frame, ServerHost server, Socket socket) {
        this.frame = frame;
        this.server = server;
        if (connect(socket)) {
            getThread().start();
            frame.addConnection(frame.w, frame.h, this);
            addLog(out, "Connection established.");
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
}
