package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.methods;
import com.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinSessionGUI extends ConnectionGUI {
    public JTextField hostField;
    public JTextField messageField;
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
        /*if (host != null) {
            if (active) {
                host.startThread();
            }
            else {
                host.endThread();
            }
        }*/
    }

    public void createConnectionLog(int w, int h) {
        super.createConnectionLog(w, h);
        JComponent[] comp;
        if (host != null) {
            comp = createLogList(host.toString(), w, h - 64);
            host.logList = (JList) comp[0];
        }
        else {
            comp = createLogList("Connection Log", w, h - 64);
        }
        connectionLog = (JList) comp[0];
        connectionScrollbar = (JScrollPane) comp[1];
        panel.add(connectionScrollbar);
        connectionLog.setBackground(Color.GRAY);
        messageField = swingMethods.createTextField("Type a message here:", panel, 0, (h * 2) + 16, w, 32);
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField field = (JTextField) e.getSource();
                if (field.getText() != null) {
                    ((ClientHost) host).setMessage(field.getText());
                    field.setText("");
                }
            }
        });
    }

    public void closeConnectionLog(int w, int h) {
        super.closeConnectionLog(panel, w, h);
        if (!(panel == null || connectionScrollbar == null)) {
            panel.remove(connectionScrollbar);
        }
    }

    public boolean attemptConnection() {
        try {
            host = new ClientHost(this, hostField.getText(), Integer.parseInt(portNumber.getText()));
            createConnectionLog(w, h);
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
