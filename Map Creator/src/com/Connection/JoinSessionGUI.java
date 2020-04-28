package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinSessionGUI extends ConnectionGUI {
    public JTextField hostField;
    public JoinSessionGUI(SessionGUI frame, int w, int h) {
        super(frame, "Join", w, h);
    }

    public void createInterface(int w, int h) {
        super.createInterface(w, h);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.joinSessionGUI = null;
            }
        });

        hostField = swingMethods.createTextField("Hostname", panel, bx - bxo, by, bw, bh);
        panel.add(hostField);
        portNumber = swingMethods.createIntegerField("Port", panel, bx + bxo - bw, by, bw, bh);
        panel.add(portNumber);
    }

    public void changeConnectionButtons(boolean active) {
        super.changeConnectionButtons(active);
        hostField.setEditable(!active);
        if (host != null) {
            if (active) {
                host.startThread();
            }
            else {
                host.endThread();
            }
        }
    }

    public void createConnectionLog(JPanel panel, int w, int h) {
        super.createConnectionLog(panel, w, h);
        JComponent[] comp = createLogList("Connection Log", w, h);
        connectionLog = (JList) comp[0];
        panel.add(comp[1]);
        connectionLog.setBackground(Color.GRAY);
    }

    public void closeConnectionLog(JPanel panel, int w, int h) {
        super.closeConnectionLog(panel, w, h);
        panel.remove(connectionLog);
        connectionLog = null;
    }

    public boolean attemptConnection() {
        try {
            host = new ClientHost(this, hostField.getText(), Integer.parseInt(portNumber.getText()));
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
