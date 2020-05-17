package com.Game.Map.Creator;

import javax.swing.*;
import java.awt.*;

public class SelectedTile extends JPanel {
    private TilesetPanel panel;
    public int x = 5;
    public int y = 5;
    public double scale = 1.2;
    public int border;
    public SelectedTile(TilesetPanel panel) {
        this.panel = panel;
        setSize((int) (panel.tileset.tileSize * scale), (int) (panel.tileset.tileSize * scale));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int i = (y * (panel.getWidth() - panel.tileset.tileSize)) + x;
        border = panel.tileset.drawTile(g, x, y, (i % panel.tileset.tw) * panel.tileset.tileSize, (i / panel.tileset.tw) * panel.tileset.tileSize, panel.tileset.tileSize, panel.tileset.tileSize, scale, Color.WHITE);
        repaint();
    }
}
