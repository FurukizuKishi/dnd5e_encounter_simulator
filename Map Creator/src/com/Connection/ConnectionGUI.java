package com.Connection;

import com.Connection.Hosts.ClientHost;
import com.Connection.Hosts.Host;
import com.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionGUI extends JFrame {
    protected SessionGUI frame;
    protected Host host;
    protected JPanel panel;
    public JTextField hostField;
    public JTextField portNumber;
    public JButton connectButton;
    protected int bx, by, bw, bh, bxo;
    public ConnectionGUI() {

    }
    public ConnectionGUI(SessionGUI frame, String action, int w, int h) {
        this.frame = frame;
        setTitle(frame.getTitle() + " - " + action + " a Session");
        createInterface(w, h);
    }

    public void createInterface(int w, int h) {
        setSize(w, h);

        panel = swingMethods.createPanel(this, 0, 0, w, h);

        bw = panel.getWidth() / 4;
        bh = 16;
        bx = (panel.getWidth() / 2);
        bxo = (panel.getWidth() / 3);
        by = (panel.getHeight() / 3);

        connectButton = swingMethods.createButton("Connect", bx - (bw / 2), by * 2, bw, bh);
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (attemptConnection()) {
                    createConnectionLog(panel, w, h);
                    ((JButton) e.getSource()).setEnabled(false);
                    if (hostField != null) {
                        hostField.setEditable(false);
                    }
                    portNumber.setEditable(false);
                }
            }
        });
        panel.add(connectButton);

        setResizable(false);
        setVisible(true);
    }

    public boolean attemptConnection() {
        return false;
    }

    public void createConnectionLog(JPanel panel, int w, int h) {
        setSize(w, (int) (h * 2.5));
        JComponent[] comp = swingMethods.createList("Connection Log", 0, h + 1, w - 8, getHeight() - h - 32, 20);
        JList log = (JList) comp[0];
        panel.add(comp[1]);
        log.setBackground(Color.GRAY);
    }
}
