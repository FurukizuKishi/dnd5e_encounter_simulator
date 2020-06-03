package com.Connection.Threads;

import com.Connection.Hosts.Host;
import com.Game.methods;

public abstract class HostQueueTaker<T> extends HostRunnable {
    protected Host<T> host;

    public void setItem() {
        setItem(null);
    }
    public abstract boolean setItem(T item);
    public abstract T getItem();
    public abstract boolean holdingItem();
    public abstract void releaseCommand();

    public boolean takeFromQueue(String endReason) {
        if (!holdingItem()) {
            if (!host.queue.isEmpty()) {
                T item = host.queue.remove();
                if (item != null) {
                    if (!setItem(item)) {
                        host.endThread(endReason);
                        return false;
                    }
                    //System.out.println(methods.tuple("MSG_TAKE", item, host.toString()));
                    continueTakeProcess();
                }
            }
        }
        return true;
    }

    public abstract void continueTakeProcess();

    public void run() {
        while (canRun()) {
            takeFromQueue("takeFromQueue()");
        }
        getThread().interrupt();
    }
}
