package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinSessionGUI extends JFrame {
    private SessionGUI frame;
    private ClientHost host;
    public JoinSessionGUI(SessionGUI frame, int w, int h) {
        this.frame = frame;
        setTitle(frame.getTitle() + " - Join a Session");
        createInterface(w, h);
    }

    public void createInterface(int w, int h) {
        setSize(w, h);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.joinSessionGUI = null;
            }
        });

        JPanel panel = swingMethods.createPanel(this, 0, 0, w, h);

        int bw = panel.getWidth() / 4;
        int bh = 16;
        int bx = (panel.getWidth() / 2);
        int bxo = (panel.getWidth() / 3);
        int by = (panel.getHeight() / 3);
        JTextField hostField = swingMethods.createTextField("Hostname", panel, bx - bxo, by, bw, bh);
        panel.add(hostField);
        JTextField portNumber = swingMethods.createIntegerField("Port", panel, bx + bxo - bw, by, bw, bh);
        panel.add(portNumber);

        JButton connectButton = swingMethods.createButton("Connect", bx - (bw / 2), by * 2, bw, bh);
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createConnectionLog(w, h);
            }
        });
        panel.add(connectButton);

        setResizable(false);
        setVisible(true);
    }

    public void createConnectionLog(int w, int h) {
        setSize(w, (int) (h * 2.5));
        JComponent[] comp = swingMethods.createList("Connection Log",0, h + 1, w, getHeight() - h, 20);
        JList log = (JList) comp[0];
        add(comp[1]);
        log.setBackground(Color.GRAY);
    }
}
