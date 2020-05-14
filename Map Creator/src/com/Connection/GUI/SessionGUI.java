package com.Connection.GUI;

import com.Game.methods;
import com.Game.swingMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SessionGUI extends JFrame {
    public CreateSessionGUI createSessionGUI = null;
    public JoinSessionGUI joinSessionGUI = null;
    public SessionGUI(String title, int w, int h) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(title);
        createInterface(w, h);
    }

    public void createInterface(int w, int h) {
        setSize(w, h);

        JPanel panel1 = swingMethods.createPanel(this, 0, 0, w / 2, h);
        panel1.setBackground(Color.RED);
        JPanel panel2 = swingMethods.createPanel(this, w / 2 + 1, 0, w / 2, h);
        panel2.setBackground(Color.BLUE);

        String[] buttonTitles = {"Create Session", "Join Session", "Exit"};
        JButton[] buttons = new JButton[buttonTitles.length];
        int squeeze = 64;
        int bx = panel2.getX() + squeeze;
        int bw = panel2.getWidth() - (squeeze * 2);
        int bh = 64;
        int bo = 8;
        int by = (panel2.getHeight() - ((bh + bo) * buttonTitles.length)) / 2;

        for (int i = 0; i < buttonTitles.length; i += 1) {
            buttons[i] = swingMethods.createButton(buttonTitles[i], bx + ((squeeze / 2) * i), by + ((bh + bo) * i), bw - ((squeeze / 2) * i), bh);
            //System.out.println(methods.tuple(bx + (32 * i), by + ((bh + bo) * i), bw - (32 * i), bh));
            panel2.add(buttons[i]);
        }

        buttons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createSession();
            }
        });

        buttons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                joinSession();
            }
        });

        buttons[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setResizable(false);
        setVisible(true);
    }

    public void createSession() {
        if (createSessionGUI == null) {
            createSessionGUI = new CreateSessionGUI(this, getWidth() / 2, getHeight() / 3);
        }
    }

    public void joinSession() {
        if (joinSessionGUI == null) {
            joinSessionGUI = new JoinSessionGUI(this, getWidth() / 2, getHeight() / 3);
        }
    }
}
