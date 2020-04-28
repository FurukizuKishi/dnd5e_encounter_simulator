package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.Connection.Hosts.ServerHost;
import com.swingMethods;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateSessionGUI extends ConnectionGUI {
    public CreateSessionGUI(SessionGUI frame, int w, int h) {
        super(frame, "Create", w, h);
    }

    public void createInterface(int w, int h) {
        super.createInterface(w, h);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.createSessionGUI = null;
            }
        });

        portNumber = swingMethods.createIntegerField("Port", panel, bx - (bw / 2), by, bw, bh);
        panel.add(portNumber);
    }

    public boolean attemptConnection() {
        try {
            host = new ServerHost(this, Integer.parseInt(portNumber.getText()));
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
