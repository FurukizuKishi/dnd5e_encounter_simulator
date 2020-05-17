package com.Game.Map.Creator;

import com.Game.GUI.SubFrame;

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
        SubFrame sheet = new SubFrame(editor.getFrame());
        sheet.removeWindowListener(sheet.windowAdapter);
        sheet.windowAdapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                sheet.close(e);
                editor.currentFilePath = currentFilePath;
            }
        };
        sheet.addWindowListener(sheet.windowAdapter);
        setLocation(0, 0);
        setVisible(true);
        setOpaque(false);
        sheet.add(this);

        sheet.setSize(getSize());
        sheet.setResizable(false);
        sheet.setVisible(true);
    }
}
