package com.Game.GUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SubFrame extends JFrame {
    private GUI frame;
    public WindowAdapter windowAdapter;
    public SubFrame(GUI frame) {
        this.frame = frame;
        frame.addSubframe(this);
        windowAdapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close(e);
            }
        };
        addWindowListener(windowAdapter);
    }

    public GUI getFrame() {
        return frame;
    }

    public void close(WindowEvent e) {
        if (frame.getSubframe((SubFrame) e.getSource()) != -1) {
            frame.removeSubframe((SubFrame) e.getSource());
        }
    }
}
