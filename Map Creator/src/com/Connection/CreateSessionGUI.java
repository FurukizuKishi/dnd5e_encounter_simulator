package com.Connection;

import com.Connection.Hosts.ServerHost;
import com.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class CreateSessionGUI extends ConnectionGUI {
    private JTabbedPane connectionTabs = new JTabbedPane();
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
                    host.getThread().end();
                }
            }
        });

        portNumber = swingMethods.createIntegerField("Port", panel, bx - (bw / 2), by, bw, bh);
        panel.add(portNumber);
    }

    public void createConnectionLog(JPanel panel, int w, int h) {
        super.createConnectionLog(panel, w, h);
        panel.add(connectionTabs);
    }

    public JList addConnection(int w, int h) {
        int n = connectionTabs.getTabCount() + 1;
        JComponent[] comp = swingMethods.createList("Connection Log", 0, h + 1, w - 8, getHeight() - h - 32, 20);
        comp[0].setBackground(Color.GRAY);
        connectionTabs.addTab("Connection " + n, comp[0]);
        connectionLogs.put(n, (JList) comp[0]);
        return (JList) comp[0];
    }

    public void closeConnectionLog(JPanel panel, int w, int h) {
        super.closeConnectionLog(panel, w, h);
        panel.remove(connectionTabs);
        connectionLogs.clear();
        connectionTabs.removeAll();
    }

    public boolean attemptConnection() {
        try {
            host = new ServerHost(this, Integer.parseInt(portNumber.getText()));
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }
}
