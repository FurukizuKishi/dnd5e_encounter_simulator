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
        connect(hostName, portNumber);
    }

    public void connect(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        try {
            connect(new Socket(hostName, portNumber));
        }
        catch (Exception e) {
            endThread(e);
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
        frame.connectButton.setEnabled(true);
        ((JoinSessionGUI) frame).hostField.setEditable(true);
        frame.portNumber.setEditable(true);
    }
}
