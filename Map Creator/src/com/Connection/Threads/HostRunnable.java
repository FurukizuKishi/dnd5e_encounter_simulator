package com.Connection.Threads;

import com.Connection.Threads.HostThread;
import com.Game.methods;

public abstract class HostRunnable implements Runnable {
    protected HostThread thread = new HostThread(this);

    public void run() {}
    public HostThread getThread() {
        return thread;
    }
    public boolean canRun() {
        if (thread != null) {
            if (thread.running) {
                return true;
            }
        }
        return false;
    }
    public void startThread() {
        thread.start();
    }
    public void endThread() {
        endThread((Exception) null);
    }
    public void endThread(Exception e) {
        thread.end();
    }
    public void endThread(String reason) {
        endThread(null, reason);
    }
    public void endThread(Exception e, String reason) {
        endThread(e);
    }
}
