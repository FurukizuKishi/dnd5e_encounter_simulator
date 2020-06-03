package com.Connection.GUI;

import com.Connection.Hosts.ClientHost;
import com.Game.methods;
import com.Game.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinSessionGUI extends ConnectionGUI {
    public JTextField hostField;
    public String clientName;
    public JoinSessionGUI(SessionGUI frame, int w, int h) {
        super(frame, "Join", w, h);
        createInterface(w, h, new JoinSessionPanel(w, h));
        ((JoinSessionPanel) panel).setGUI(this);
    }

    public void createInterface(int w, int h, JPanel panel) {
        super.createInterface(w, h, panel);
        System.out.println(methods.tuple(2, this.panel));
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.joinSessionGUI = null;
            }
        });

        hostField = swingMethods.createTextField("Hostname", this.panel, bx - bxo, by, bw, bh);
        this.panel.add(hostField);
        portNumber = swingMethods.createIntegerField("Port", this.panel, bx + bxo - bw, by, bw, bh);
        this.panel.add(portNumber);
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
        //System.out.println(methods.tuple(91, this));
        if (!(panel == null || connectionScrollbar == null)) {
            panel.remove(connectionScrollbar);
        }
    }

    public boolean attemptConnection(boolean master) {
        try {
            createConnectionLog(w, h);
            host = new ClientHost(this, hostField.getText(), Integer.parseInt(portNumber.getText()), master);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
