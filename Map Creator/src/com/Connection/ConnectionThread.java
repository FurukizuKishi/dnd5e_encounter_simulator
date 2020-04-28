package com.Connection;

public class ConnectionThread extends Thread {
    public volatile boolean running = false;

    public ConnectionThread(Runnable target) {
        super(target);
    }

    public void start() {
        super.start();
        running = true;
    }

    public void end() {
        try {
            running = false;
            interrupt();
            join();
        }
        catch (InterruptedException e) {

        }
    }
}
