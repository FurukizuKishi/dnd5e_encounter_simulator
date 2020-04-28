package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.swingMethods;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinSessionGUI extends ConnectionGUI {
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
