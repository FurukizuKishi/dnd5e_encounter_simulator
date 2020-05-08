package com.Connection;

import com.Connection.Hosts.ServerHost;
import com.Connection.Hosts.SingleHost;
import com.swingMethods;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class CreateSessionGUI extends ConnectionGUI {
    public JTabbedPane connectionTabs = new JTabbedPane();
    private HashMap<Integer, JList> connectionLogs = new HashMap<>();

    public CreateSessionGUI(SessionGUI frame, int w, int h) {
        super(frame, "Create", w, h);
    }

    public void createInterface(int w, int h) {
        super.createInterface(w, h);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.createSessionGUI = null;
                if (host != null) {
                    host.endThread();
                }
            }
        });

        portNumber = swingMethods.createIntegerField("Port", panel, bx - (bw / 2), by, bw, bh);
        panel.add(portNumber);
    }

    public void changeConnectionButtons(boolean active) {
        super.changeConnectionButtons(active);
        if (host != null) {
            if (active) {
                host.startThread();
            } else {
                host.endThread();
            }
        }
    }

    public void createConnectionLog(int w, int h) {
        super.createConnectionLog(w, h);
        connectionTabs.setSize(w, h);
        connectionTabs.setLocation(0, h + 1);
        panel.add(connectionTabs);
        repaint();
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
        connectionLogs.put(n, (JList) comp[0]);
        host.logList = (JList) comp[0];
        return (JList) comp[0];
    }

    public void closeConnectionLog(int w, int h) {
        super.closeConnectionLog(w, h);
        panel.remove(connectionTabs);
        connectionLogs.clear();
    }

    public boolean attemptConnection() {
        try {
            host = new ServerHost(this, Integer.parseInt(portNumber.getText()));
            createConnectionLog(w, h);
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }
}
