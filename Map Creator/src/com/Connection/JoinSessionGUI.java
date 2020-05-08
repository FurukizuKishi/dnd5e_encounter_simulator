package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinSessionGUI extends ConnectionGUI {
    public JTextField hostField;
    public String clientName;
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
        alterConnectionThread(active);
    }

    public void createConnectionLog(int w, int h) {
        super.createConnectionLog(w, h);
        JComponent[] comp;
        if (host != null) {
            comp = createLogList(host.toString(), w, h);
        }
        else {
            comp = createLogList("Connection Log", w, h);
        }
        connectionLog = (JList) comp[0];
        connectionScrollbar = (JScrollPane) comp[1];
        panel.add(connectionScrollbar);
        connectionLog.setBackground(Color.GRAY);
    }

    public void closeConnectionLog(int w, int h) {
        super.closeConnectionLog(panel, w, h);
        if (!(panel == null || connectionScrollbar == null)) {
            panel.remove(connectionScrollbar);
        }
    }

    public boolean attemptConnection() {
        try {
            createConnectionLog(w, h);
            host = new ClientHost(this, hostField.getText(), Integer.parseInt(portNumber.getText()));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
