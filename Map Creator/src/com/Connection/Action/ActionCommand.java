package com.Connection.Action;

import com.Connection.Hosts.ServerThreadHost;
import com.Game.methods;

public class ActionCommand {
    public String command;
    public ServerThreadHost host;
    public String result = null;
    public String[] recipients;
    private boolean replaceable = true;
    private boolean processed = false;

    public ActionCommand(ServerThreadHost host, String command) {
        this.host = host;
        this.command = command;
    }
    public ActionCommand(ServerThreadHost host, String command, String ... recipients) {
        this.host = host;
        if (recipients.length > 0) {
            this.command = recipients[0];
            for (int i = 1; i < recipients.length; i += 1) {
                command += "," + recipients[i];
            }
            String[] split = command.split(":");
            for (int i = 1; i < split.length; i += 1) {
                command += ":" + split[i];
            }
        }
        setRecipients(recipients);
    }

    public void setReplaceable(boolean replaceable) {
        this.replaceable = replaceable;
    }
    public boolean isReplaceable() {
        return replaceable;
    }

    public void process(boolean processed) {
        this.processed = processed;
    }
    public boolean isProcessed() {
        return processed;
    }

    public void setRecipients(String ... recipients) {
        this.recipients = recipients;
    }

    public String toString() {
        if (result != null) {
            return "R: '" + result + "'";
        }
        return "C: '" + command + "'";
    }
}
