package com.Map.Creator;

import com.GUI.Camera;
import com.GUI.GUI;
import com.Map.Map;
import com.System.InputMethods.MouseInput;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class EditorFrame extends JFrame {
    JToolBar toolbar = new JToolBar();
    TilesetPanel tilePanel;
    Camera camera;
    Map map;
    int bw = 8;
    int bh = 32;
    public EditorFrame(GUI frame, Map map) {
        setSize(frame.getSize());
        this.map = map;
        this.addMouseListener(new MouseInput());
        this.addMouseMotionListener(new MouseInput());
        createToolbar();
        createInterface();
    }
    public EditorFrame(GUI frame) {
        this(frame, frame.camera.getMap());
    }
    public void createToolbar() {
        setLayout(null);

        toolbar.add(new JButton("Hello"));
        toolbar.addSeparator();
        toolbar.add(new JButton("Hello"));
        toolbar.setVisible(true);
        toolbar.setLocation(0, 0);
        toolbar.setSize(getWidth(), 30);
        add(toolbar);
    }
    public void createInterface() {
        setLayout(null);

        int h = getHeight() - bh - toolbar.getHeight();
        tilePanel = new TilesetPanel("Cave");
        tilePanel.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Tiles"), new BevelBorder(1)));
        tilePanel.setLocation(0, toolbar.getHeight() + 1);
        tilePanel.setSize((int) (getWidth() * 0.2), h);
        add(tilePanel);
        tilePanel.setVisible(true);

        camera = new Camera(this, map);
        add(camera);
        camera.setVisible(true);
        JScrollPane cameraBar = new JScrollPane(camera);
        cameraBar.setLocation(tilePanel.getWidth(), toolbar.getHeight() + 1);
        cameraBar.setSize(getWidth() - tilePanel.getWidth() - bw, h);
        cameraBar.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Map"), new BevelBorder(1)));
        add(cameraBar);
        cameraBar.setVisible(true);

        setResizable(false);
        setVisible(true);
    }
}
