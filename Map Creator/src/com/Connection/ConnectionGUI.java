package com.Connection;

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
    public JTextField portNumber;
    public JButton connectButton;
    public JButton terminateButton;
    public JList connectionLog;
    public JScrollPane connectionScrollbar;
    public int w, h;
    protected int bx, by, bw, bh, bxo;
    public ConnectionGUI() {

    }
    public ConnectionGUI(SessionGUI frame, String action, int w, int h) {
        this.frame = frame;
        setTitle(frame.getTitle() + " - " + action + " a Session");
        createInterface(w, h);
    }

    public void createInterface(int w, int h) {
        this.w = w;
        this.h = h;
        setSize(w, h);

        panel = swingMethods.createPanel(this, 0, 0, w, h);

        bw = panel.getWidth() / 4;
        bh = 16;
        bx = (panel.getWidth() / 2);
        bxo = (panel.getWidth() / 3);
        by = (panel.getHeight() / 3);

        connectButton = swingMethods.createButton("Connect", bx - (bw / 2), (by * 2) - 32, bw, bh);
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (attemptConnection()) {
                    changeConnectionButtons(true);
                }
            }
        });
        panel.add(connectButton);

        terminateButton = swingMethods.createButton("Terminate", bx - (bw / 2), by * 2, bw, bh);
        terminateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeConnectionLog(w, h);
                changeConnectionButtons(false);
            }
        });
        terminateButton.setEnabled(false);
        panel.add(terminateButton);

        setResizable(false);
        setVisible(true);
    }

    public boolean attemptConnection() {
        return false;
    }

    public void changeConnectionButtons(boolean active) {
        terminateButton.setEnabled(active);
        connectButton.setEnabled(!active);
        portNumber.setEditable(!active);
    }

    public void createConnectionLog(int w, int h) {
        setSize(w, (int) (h * 2.5));
    }

    public JComponent[] createLogList(String title, int w, int h) {
        JComponent[] comp = swingMethods.createList(title, 0, h + 1, w - 8, getHeight() - h - 64, 20);
        comp[0].setBackground(Color.GRAY);
        return comp;
    }

    public void closeConnectionLog(int w, int h) {
        closeConnectionLog(null, w, h);
    }
    public void closeConnectionLog(JPanel panel, int w, int h) {
        setSize(w, h);
    }
}
