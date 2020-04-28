package com.Connection.Hosts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SingleHost extends Host {
    protected Socket socket;
    protected BufferedReader stdIn;

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
    public boolean connect(Socket socket) {
        try {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            return true;
        }
        catch (Exception e) {
            endThread(e);
            return false;
        }
    }

    public void endThread(Exception e) {
        super.endThread(e);
    }

    public void run() {
        while (canRun()) {
            if (frame.connectionLog != null) {
                try {
                    String input;
                    while (in != null) {
                        if ((input = in.readLine()) != null) {
                            System.out.println("Server: " + input);
                            addLog(out, input);

                            String cInput = stdIn.readLine();
                            if (cInput != null) {
                                System.out.println("Client: " + cInput);
                            }
                        }
                    }
                } catch (IOException e) {
                    addLog(out, "[ERR]: " + e.getMessage());
                    endThread(e);
                }
            }
        }
    }
}
