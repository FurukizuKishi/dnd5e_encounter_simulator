package com.Connection.Hosts;

import com.Connection.JoinSessionGUI;

import java.io.IOException;
import java.net.Socket;

public class ClientHost extends SingleHost {
    public ClientHost(JoinSessionGUI frame) {
        this.frame = frame;
        logList = frame.connectionLog;
    }
    public ClientHost(JoinSessionGUI frame, Socket socket) {
        this(frame);
        connect(socket);
    }
    public ClientHost(JoinSessionGUI frame, String hostName, int portNumber) {
        this(frame);
        if (connect(hostName, portNumber)) {
            getThread().start();
            addLog(out, "Connection received.");
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
}
