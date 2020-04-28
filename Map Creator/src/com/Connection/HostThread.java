package com.Connection;

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
            interrupt();
            join();
        }
        catch (InterruptedException e) {

        }
    }
}
