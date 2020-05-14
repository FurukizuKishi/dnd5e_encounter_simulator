package com.Game.Map.Creator;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SaveMap extends JPanel {
    EditorFrame editor;
    String currentFilePath;
    public SaveMap(EditorFrame editor) {
        this.editor = editor;
        setSize(480, 240);
    }

    public void createInterface() {
        JFrame sheet = new JFrame();
        sheet.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                editor.currentFilePath = currentFilePath;
            }
        });
        setLocation(0, 0);
        setVisible(true);
        setOpaque(false);
        sheet.add(this);

        sheet.setSize(getSize());
        sheet.setResizable(false);
        sheet.setVisible(true);
    }
}
