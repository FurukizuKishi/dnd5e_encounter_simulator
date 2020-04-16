package com.Map.Creator;

import com.GUI.System.Background;

import javax.swing.*;
import java.awt.*;

public class TilesetPanel extends JPanel {
    public Background tileset;
    private JScrollPane scrollbar;
    public TilesetPanel() {
        scrollbar = new JScrollPane(this);
        scrollbar.setPreferredSize(getPreferredSize());
        scrollbar.setVisible(true);
    }
    public TilesetPanel(String spritesheet) {
        this();
        tileset = new Background(spritesheet, null, 32);
    }

    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        scrollbar.setPreferredSize(getPreferredSize());
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tileset.tileSheet != null) {
            int th = (tileset.tileSheet.getWidth(null) * tileset.tileSheet.getHeight(null)) / tileset.tileSize;
            int i = 0;
            int y = 0;
            while (i < th) {
                for (int x = 0; x < getWidth(); x += tileset.tileSize) {
                    tileset.drawTile(g, x, y, Color.WHITE);
                    i += 1;
                }
                y += 1;
            }
        }
    }
}
