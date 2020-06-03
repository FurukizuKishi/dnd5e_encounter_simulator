package com.Connection.Action;

import com.Connection.Hosts.Host;
import com.Connection.Threads.HostQueueTaker;
import com.Game.methods;

public abstract class ActionProtocol<T> extends HostQueueTaker<T> {
    protected T command = null;

    public void continueTakeProcess() {
        processCommand();
        releaseCommand();
    }

    public void setItem() {
        this.command = null;
    }
    public T getItem() {
        return command;
    }
    public boolean holdingItem() {
        return (command != null);
    }

    public boolean setItem(T command) {
        if (command != null) {
            //System.out.println(methods.tuple("SET", this, command));
            this.command = command;
            return true;
        }
        return false;
    }
    public void releaseCommand() {
        //System.out.println(methods.tuple("RELEASE", this, command));
        setItem();
    }

    public abstract void processCommand();
}
