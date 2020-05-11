package com.Map.Creator;

import com.GUI.Camera;
import com.GUI.GUI;
import com.Map.Map;
import com.System.InputMethods.MouseInput;
import com.swingMethods;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

public class EditorFrame extends JFrame {
    JToolBar toolbar = new JToolBar();
    TilesetPanel tilePanel;
    JList layerList;
    Camera camera;
    Map map;
    String currentFilePath;
    int bw = 8;
    int bh = 32;
    int tileSize = 32;
    public EditorFrame(GUI frame, Map map) {
        setSize(frame.getSize());
        this.map = new Map(map.map, map.layers);
        this.map.setEditing(true);
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
        JComponent[] comp;

        tilePanel = swingMethods.createTilesetPanel(this, "Cave", bw, (int) (bh * 0.6667), (int) Math.ceil((getWidth() * 0.2) / tileSize) * tileSize, (int) (h * 0.5), tileSize);

        JPanel panel1 = swingMethods.createPanel(this, "Tiles", 0, toolbar.getHeight() + 1, tilePanel.getWidth() - (bw * 2), tilePanel.getHeight());
        comp = swingMethods.createLayeredPane(0, 0, tilePanel.getWidth(), tilePanel.getHeight(), tilePanel.tile, tilePanel);
        panel1.add(comp[1]);

        JPanel panel2 = swingMethods.createPanel(this, "Layers", 0, toolbar.getHeight() + panel1.getHeight() + 1, panel1.getWidth(), h - panel1.getHeight());
        comp = swingMethods.createList("Layers", 0, 0, panel2.getWidth(), panel2.getHeight(), 30, "Layer 1", "Layer 2", "Layer 3");
        layerList = (JList) comp[0];
        panel2.add(comp[1]);

        comp = swingMethods.createCamera(this, "Map", map, tilePanel.getWidth() - bw, toolbar.getHeight() + 1, getWidth() - tilePanel.getWidth(), h);;
        camera = (Camera) comp[0];
        add(comp[1]);

        setResizable(false);
        setVisible(true);
    }
}
