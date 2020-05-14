package com.Connection.Threads;

import com.Connection.Hosts.SingleHost;
import com.Game.methods;

import java.io.BufferedReader;
import java.io.IOException;

public class ReceiveThread extends HostRunnable {
    private SingleHost host;
    private BufferedReader in;
    public ReceiveThread(SingleHost host, BufferedReader in) {
        this.host = host;
        this.in = in;
        startThread();
    }

    public String toString() {
        if (host.name != null) {
            return getClass().getSimpleName() + ":" + host.name;
        }
        return super.toString();
    }

    public boolean isRunning() {
        return (in != null && canRun());
    }
    public void setInput(BufferedReader in) {
        this.in = in;
    }

    public void receiveMessage() {
        try {
            String input;
            if ((input = in.readLine()) != null) {
                System.out.println(methods.tuple("RCV", input, host));
                host.receiveMessage(input);
                if (methods.messageIsFlag(input)) {
                    host.addCommand(input);
                }
            }
        }
        catch (IOException e) {
            host.endThread(e, "receiveMessage()");
        }
    }

    @Override
    public void endThread() {
        super.endThread();
        try {
            if (in != null) {
                in.close();
                in = null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (canRun()) {
            while (in != null) {
                receiveMessage();
            }
            endThread("run()");
        }
        getThread().interrupt();
    }
}
