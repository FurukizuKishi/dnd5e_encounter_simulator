package com.Map.Creator;

import com.GUI.Camera;
import com.GUI.GUI;
import com.Map.Map;
import com.System.InputMethods.MouseInput;

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
        this.map = new Map(map.map, map.background);
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

    public JPanel createPanel(String title, int x, int y, int w, int h) {
        JPanel panel = new JPanel();
        panel.setLocation(x, y);
        panel.setBorder(createBorder(title));
        panel.setSize(w, h);
        panel.setVisible(true);
        add(panel);
        return panel;
    }
    public TilesetPanel createTilesetPanel(String tilesheet, int x, int y, int w, int h, int ts) {
        TilesetPanel panel = new TilesetPanel(this, tilesheet);
        panel.tileset.tileSize = ts;
        panel.setOpaque(false);
        panel.setLocation(x, y);
        panel.setSize(w, h);
        panel.setVisible(true);
        return panel;
    }
    public JComponent[] createLayeredPane(int x, int y, int w, int h, JComponent ... layers) {
        JLayeredPane pane = new JLayeredPane();
        pane.setLocation(x, y);
        pane.setSize(w, h);
        pane.setPreferredSize(pane.getSize());
        for (int i = 0; i < layers.length; i += 1) {
            pane.add(layers[i], new Integer(i + 1));
        }
        pane.setVisible(true);

        JScrollPane bar = createScrollpane(pane, x, y, w, h);
        bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return new JComponent[] {pane, bar};
    }
    public JComponent[] createLayers(String title, int x, int y, int w, int h, int ch, String ... layers) {
        JList list = new JList(layers);
        list.setDragEnabled(true);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellWidth(w);
        list.setFixedCellHeight(ch);
        list.setSize(w, h);

        JScrollPane bar = createScrollpane(list, x, y, w, h);
        bar.setBorder(createBorder(title));
        bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return new JComponent[] {list, bar};
    }
    public JComponent[] createCamera(String title, JFrame frame, Map map, int x, int y, int w, int h) {
        Camera camera = new Camera(this, map);
        frame.add(camera);
        camera.setVisible(true);

        JScrollPane bar = createScrollpane(camera, x, y, w, h);
        bar.setBorder(createBorder(title));
        bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return new JComponent[] {camera, bar};
    }
    public JScrollPane createScrollpane(JComponent view, int x, int y, int w, int h) {
        JScrollPane bar = new JScrollPane(view);
        bar.setLocation(x, y);
        bar.setSize(w, h);
        bar.setVisible(true);
        return bar;
    }
    public CompoundBorder createBorder(String title) {
        return BorderFactory.createCompoundBorder(new TitledBorder(title), new BevelBorder(1));
    }
    public void createInterface() {
        setLayout(null);
        int h = getHeight() - bh - toolbar.getHeight();
        JComponent[] comp;

        tilePanel = createTilesetPanel("Cave", bw, (int) (bh * 0.6667), (int) Math.ceil((getWidth() * 0.2) / tileSize) * tileSize, (int) (h * 0.5), tileSize);

        JPanel panel1 = createPanel("Tiles", 0, toolbar.getHeight() + 1, tilePanel.getWidth() - (bw * 2), tilePanel.getHeight());
        comp = createLayeredPane(0, 0, tilePanel.getWidth(), tilePanel.getHeight(), tilePanel.tile, tilePanel);
        panel1.add(comp[1]);

        JPanel panel2 = createPanel("Layers", 0, toolbar.getHeight() + panel1.getHeight() + 1, panel1.getWidth(), h - panel1.getHeight());
        comp = createLayers("Layers", 0, 0, panel2.getWidth(), panel2.getHeight(), 30, "Layer 1", "Layer 2", "Layer 3");
        layerList = (JList) comp[0];
        panel2.add(comp[1]);

        comp = createCamera("Map", this, map, tilePanel.getWidth() - bw, toolbar.getHeight() + 1, getWidth() - tilePanel.getWidth(), h);;
        camera = (Camera) comp[0];
        add(comp[1]);

        setResizable(false);
        setVisible(true);
    }
}
