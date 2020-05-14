package com.Connection.GUI;

import com.Connection.Hosts.ServerHost;
import com.Connection.Hosts.ServerThreadHost;
import com.Connection.Hosts.SingleHost;
import com.Game.methods;
import com.Game.swingMethods;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class CreateSessionGUI extends ConnectionGUI {
    public JTabbedPane connectionTabs = new JTabbedPane();
    private HashMap<String, JList> connectionLogs = new HashMap<>();
    public JoinSessionGUI clientFrame;

    public CreateSessionGUI(SessionGUI frame, int w, int h) {
        super(frame, "Create", w, h);
    }

    public String getConnectionKey(ServerThreadHost host) {
        String key = null;
        for (Map.Entry<String, JList> log : connectionLogs.entrySet()) {
            //System.out.println(log.getValue() == host.logList);
            if (log.getValue() == host.logList) {
                key = log.getKey();
            }
        }
        return key;
    }
    public void renameConnectionLog(ServerThreadHost host, String name) {
        String key = getConnectionKey(host);
        //System.out.println(methods.tuple("logs", connectionLogs.size()));
        String tabName = name;
        if (((ServerHost) this.host).master == host) {
            tabName += " (DM)";
        }
        if (key != null) {
            host.name = name;
            int id = Integer.parseInt(key);
            String previousTabName = "Connection " + id;
            if (host.nameAssigned) {
                id = host.pos;
                previousTabName = host.name;
            }
            connectionTabs.setTitleAt(id - 1, tabName);
            connectionLogs.put(name, connectionLogs.get(key));
            connectionLogs.remove(host.toString());
            connectionLogs.remove(previousTabName);
            //System.out.println(methods.tuple("key", id - 1, previousTabName));
        }
    }

    public void createInterface(int w, int h) {
        super.createInterface(w, h);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.createSessionGUI = null;
                if (host != null) {
                    host.endThread("createInterface()");
                }
            }
        });

        portNumber = swingMethods.createIntegerField("Port", panel, bx - (bw / 2), by, bw, bh);
        panel.add(portNumber);
    }

    public void changeConnectionButtons(boolean active) {
        super.changeConnectionButtons(active);
        alterConnectionThread(active);
    }

    public void createConnectionLog(int w, int h) {
        super.createConnectionLog(w, h);
        connectionTabs.setSize(w, h);
        connectionTabs.setLocation(0, h + 1);
        panel.add(connectionTabs);
    }

    public JList addConnection(int w, int h, SingleHost host) {
        int n = connectionTabs.getTabCount() + 1;
        JComponent[] comp = createLogList(null, w, h);
        if (host != null) {
            connectionTabs.addTab(host.toString(), comp[1]);
        }
        else {
            connectionTabs.addTab("Connection " + n, comp[1]);
        }
        connectionLogs.put(Integer.toString(n), (JList) comp[0]);
        host.logList = (JList) comp[0];
        return (JList) comp[0];
    }

    public void closeConnectionLog(int w, int h) {
        super.closeConnectionLog(w, h);
        panel.remove(connectionTabs);
        connectionLogs.clear();
    }

    public boolean attemptConnection(boolean master) {
        try {
            host = new ServerHost(this, Integer.parseInt(portNumber.getText()));
            createConnectionLog(w, h);
            return true;
        }
        catch (Exception e) {
            //System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }
}
