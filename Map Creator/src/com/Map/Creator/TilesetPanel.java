package com.Map.Creator;

import com.GUI.System.Background;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TilesetPanel extends JPanel {
    private EditorFrame frame;
    public Background tileset;
    public SelectedTile tile;
    private JScrollPane scrollbar = new JScrollPane(this);
    public TilesetPanel(EditorFrame frame) {
        this.frame = frame;
        scrollbar.setPreferredSize(getPreferredSize());
        scrollbar.setVisible(true);
    }
    public TilesetPanel(EditorFrame frame, String spritesheet) {
        this(frame);
        setOpaque(true);
        tileset = new Background(spritesheet, null, 32);
        tile = new SelectedTile(this);
    }

    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        scrollbar.setPreferredSize(getPreferredSize());
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] selectedTile = {tile.x, tile.y};
        if (tileset.tileSheet != null) {
            int th = (tileset.tileSheet.getWidth(null) * tileset.tileSheet.getHeight(null)) / tileset.tileSize;
            int i = 0;
            int y = 0;
            while (i < th) {
                for (int x = 0; x < getWidth() - tileset.tileSize; x += tileset.tileSize) {
                    double scale = 1;
                    if (Arrays.equals(selectedTile, new int[] {x, y})) {
                        scale = 1.2;
                    }
                    tileset.drawTile(g, getX() + x, getY() + y, (i % tileset.tw) * tileset.tileSize, (i / tileset.tw) * tileset.tileSize, scale, Color.WHITE);
                    i += 1;
                }
                y += tileset.tileSize;
            }
        }
    }
}
