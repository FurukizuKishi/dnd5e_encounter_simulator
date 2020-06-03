package com.Connection.GUI;

import com.Connection.Hosts.ClientHost;
import com.Game.methods;
import com.Game.swingMethods;

import javax.swing.*;
import java.awt.*;

public class JoinSessionPanel extends JPanel {
    JoinSessionGUI gui = null;
    public JoinSessionPanel(int w, int h) {
        swingMethods.alterPanel(this, 0, 0, w, h);
        System.out.println(methods.tuple(getX(), getY(), getWidth(), getHeight()));
        setVisible(true);
    }
    public void setGUI(JoinSessionGUI gui) {
        this.gui = gui;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gui != null) {
            if (gui.host != null) {
                ClientHost client = (ClientHost) gui.host;
                boolean[] lights = {client.holdingFlag(), client.sentFlag, client.holdingFlag() && !client.sentFlag};
                String[] labels = {client.getMessage(), "client.sentFlag", "client.holdingFlag() && !client.sentFlag"};
                System.out.println(methods.tuple(lights));
                Color colour;
                for (int i = 0; i < lights.length; i += 1) {
                    colour = Color.RED;
                    if (lights[i]) {
                        colour = Color.GREEN;
                    }
                    g.setColor(colour);
                    g.fillRect(8, 8 + (i * 24), 16, 16);
                    g.setColor(Color.BLACK);
                    if (labels[i] != null) {
                        g.drawString(labels[i], 40, 22 + (i * 24));
                    }
                }
            }
        }
    }
}
