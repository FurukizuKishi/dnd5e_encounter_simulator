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
    public ServerThreadHost(CreateSessionGUI frame, ServerHost server, Socket socket) {
        this.frame = frame;
        this.server = server;
        if (connect(socket)) {
            getThread().start();
            logList = frame.addConnection(frame.w, frame.h, this);
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

    public void run() {
        while (canRun()) {
            if (connectionLog != null) {
                try {
                    String input;
                    int i = 0;
                    while (in != null) {
                        TimeUnit.SECONDS.sleep(3);
                        addLog(out, Integer.toString(i));
                        /*Object[] stream = in.lines().toArray();
                        if (stream.length > 0) {
                            if ((input = (String) stream[stream.length - 1]) != null) {
                                addLog(input);
                                if (input.contains("ERR")) {
                                    break;
                                }
                            }
                        }*/
                        if ((input = in.readLine()) != null) {
                            addLog(input);
                            if (input.contains("ERR")) {
                                addLog(out, "Error Received.");
                                break;
                            }
                        }
                        i += 1;
                    }
                    endThread();
                } catch (Exception e) {
                    endThread(e);
                }
            }
        }
        getThread().interrupt();
    }
}
