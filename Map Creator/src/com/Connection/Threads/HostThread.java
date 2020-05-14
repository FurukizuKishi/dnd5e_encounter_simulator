package com.Connection.Threads;

public class HostThread extends Thread {
    public volatile boolean running = false;

    public HostThread(Runnable target) {
        super(target);
    }

    public void start() {
        super.start();
        running = true;
    }

    public void end() {
        try {
            running = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
