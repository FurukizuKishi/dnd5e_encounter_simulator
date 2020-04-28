package com.Connection.Hosts;

import com.Connection.CreateSessionGUI;
import com.Connection.JoinSessionGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadHost extends SingleHost {
    public ServerThreadHost(CreateSessionGUI frame, Socket socket) {
        this.frame = frame;
        if (connect(socket)) {
            frame.addConnection(frame.w, frame.h);
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
