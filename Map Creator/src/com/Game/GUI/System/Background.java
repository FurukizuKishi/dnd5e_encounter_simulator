package com.Game.GUI.System;

import com.Game.Map.Map;
import com.Game.System.Enums;
import com.Game.methods;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static javafx.application.Platform.exit;

public class Background {
    public String backgroundPath = "backgrounds/";                      //The backgrounds filepath.
    public Image tileSheet;                                             //The tiles the background uses to draw into the screen.
    public boolean autotiled = true;                                    //Whether the background is autotiled.
    public Image background;                                            //The image used as the map's background.
    public int tw;                                                      //How many tiles to record on a line of the sheet before moving into the next.
    public Map map;                                                     //The map the background is assigned to.
    public String name;                                                 //The filename of the background's tilesheet.
    public int tileSize = 32;                                           //The dimensions of a tile in the tilesheet.
    public int[][][] tileGrid;                                          //The grid containing the positions of tiles in the background's room.
    public HashMap<String, int[]> tileMap = new HashMap<>();            //All of the wall and floor tiles.
    public HashMap<String, int[]> doorMap = new HashMap<>();            //All of the door entrance and exit tiles.

    //Background constructor. Fetch the filename of the tilesheet, or the image for the background.
    public Background(String tilesheet, Map map) {
        this.name = tilesheet;
        this.map = map;
        getTilesheet();
    }
    public Background(String tilesheet, Map map, int tileSize) {
        this(tilesheet, map);
        this.tileSize = tileSize;
    }
    public Background(BufferedImage background, Map map) {
        this.background = background;
        this.map = map;
    }
    public Background(BufferedImage background, Map map, int tileSize) {
        this(background, map);
        this.tileSize = tileSize;
    }

    public void setBackground(BufferedImage background) {
        this.background = background;
    }

    //Set the tile size.
    public void setTilesheet(BufferedImage tileSheet, int tileSize) {
        this.tileSheet = tileSheet;
        this.tileSize = tileSize;
        if (tileSheet != null) {
            this.tw = tileSheet.getWidth(null) / tileSize;
        }
    }

    //Fetch the tilesheet for the background.
    public void getTilesheet() {
        this.backgroundPath += name;
        //System.out.println("[DBG]: " + name + " background - " + this.backgroundPath + ".png");
        try {
            setTilesheet(methods.getImage(backgroundPath), tileSize);
            initializeTileMap();
        }
        catch (Exception e) {
            //System.out.println("[ERR]: " + e + " " +  backgroundPath);
            setTilesheet(methods.getImage(backgroundPath), tileSize);
            //System.out.println(tileSheet);
            exit();
        }
    }

    //Set up the code mappings for each terrain tile in the tilesheet.
    public void initializeTileMap() {
        ArrayList<int[]> tiles = new ArrayList<>();
        ArrayList<int[]> doors = new ArrayList<>();
        //Top, Bottom, Left, Right
        tiles.add(new int[] {1, 1, 1, 1});
        tiles.add(new int[] {1, 0, 1, 1});
        tiles.add(new int[] {0, 1, 1, 1});
        tiles.add(new int[] {1, 1, 0, 1});
        tiles.add(new int[] {1, 1, 1, 0});
        tiles.add(new int[] {0, 0, 1, 1});
        tiles.add(new int[] {1, 1, 0, 0});
        tiles.add(new int[] {1, 0, 0, 0});
        tiles.add(new int[] {0, 1, 0, 0});
        tiles.add(new int[] {0, 0, 0, 1});
        tiles.add(new int[] {0, 0, 1, 0});
        tiles.add(new int[] {0, 1, 0, 1});
        tiles.add(new int[] {0, 1, 1, 0});
        tiles.add(new int[] {1, 0, 0, 1});
        tiles.add(new int[] {1, 0, 1, 0});
        tiles.add(new int[] {1, 1, 1, 1, 1, 1, 1, 0});
        tiles.add(new int[] {1, 1, 1, 1, 1, 1, 0, 1});
        tiles.add(new int[] {1, 1, 1, 1, 1, 0, 1, 1});
        tiles.add(new int[] {1, 1, 1, 1, 0, 1, 1, 1});
        tiles.add(new int[] {0, 1, 0, 1, 0, 0, 0, 1});
        tiles.add(new int[] {0, 1, 1, 0, 0, 0, 1, 0});
        tiles.add(new int[] {1, 0, 0, 1, 0, 1, 0, 0});
        tiles.add(new int[] {1, 0, 1, 0, 1, 0, 0, 0});
        tiles.add(new int[] {1, 1, 0, 1, 0, 0, 0, 1});
        tiles.add(new int[] {1, 1, 1, 0, 0, 0, 1, 0});
        tiles.add(new int[] {1, 1, 0, 1, 0, 1, 0, 0});
        tiles.add(new int[] {1, 1, 1, 0, 1, 0, 0, 0});
        tiles.add(new int[] {0, 1, 1, 1, 0, 0, 0, 1});
        tiles.add(new int[] {0, 1, 1, 1, 0, 0, 1, 0});
        tiles.add(new int[] {1, 0, 1, 1, 0, 1, 0, 0});
        tiles.add(new int[] {1, 0, 1, 1, 1, 0, 0, 0});
        tiles.add(new int[] {1, 1, 1, 1, 0, 0, 1, 1});
        tiles.add(new int[] {1, 1, 1, 1, 1, 1, 0, 0});
        tiles.add(new int[] {1, 1, 1, 1, 0, 1, 0, 1});
        tiles.add(new int[] {1, 1, 1, 1, 1, 0, 1, 0});
        tiles.add(new int[] {1, 1, 1, 1, 1, 1, 1, 1});
        tiles.add(new int[] {0, 0, 0, 0});
        tiles.add(new int[] {2});
        for (int i = 1; i < tiles.size() + 1; i += 1) {
            String key = map.wallChecker.concatenateCode(tiles.get(i - 1));
            int[] value = new int[] {i % tw, i / tw};
            //System.out.println(methods.tuple("tile", key, Arrays.toString(value)));
            tileMap.put(key, value);
        }

        //Do the same for the door tiles.
        doors.add(new int[] {1, 0, 2});
        doors.add(new int[] {1, 2, 2});
        doors.add(new int[] {2, 1, 2});
        doors.add(new int[] {0, 1, 2});
        doors.add(new int[] {1, 0, 1});
        doors.add(new int[] {1, 2, 1});
        doors.add(new int[] {2, 1, 1});
        doors.add(new int[] {0, 1, 1});
        doors.add(new int[] {1, 0, 0});
        doors.add(new int[] {1, 2, 0});
        doors.add(new int[] {2, 1, 0});
        doors.add(new int[] {0, 1, 0});
        for (int i = 0; i < doors.size(); i += 1) {
            int[] value = new int[] {i % tw, i / tw};
            String key = map.wallChecker.concatenateCode(doors.get(i));
            //System.out.println(methods.tuple("door", key, Arrays.toString(value)));
            doorMap.put(key, value);
        }
    }

    //Draw the default square boxes for the walls if no tiles are available.
    public void drawWall(Graphics g, int tileX, int tileY, int screenX, int screenY, int screenTileW, int screenTileH) {
        String text = "";
        if (map.isWall(tileX, tileY)) {
            g.setColor(new Color(55, 55, 55, 105));
            text = "Wall";
        }
        else if (map.isDifficultTerrain(tileX, tileY)) {
            g.setColor(new Color(55, 55, 55, 255));
            text = "Slow";
        }
        else if (map.isWater(tileX, tileY)) {
            g.setColor(new Color(55, 105, 125, 55));
            text = "Water";
        }
        else {
            g.setColor(BLACK);
        }
        if (g.getColor() != BLACK) {
            int[] td = getTileDimensions(0, 0, tileSize, tileSize, screenX, screenY, screenTileW, screenTileH);
            g.fillRect(td[4], td[5], td[6] - td[4], td[7] - td[5]);
            if (map.canEdit()) {
                methods.drawString(g, text, screenX - 1 + (screenTileW / 2), screenY + (screenTileH / 2), Fonts.font.TEXT, WHITE, 4);
            }
        }
    }

    //Draw the repeating pattern for the room's floor.
    public void drawRoomFloor(Graphics g, int tileX, int tileY, int screenX, int screenY, int screenTileW, int screenTileH) {
        //System.out.println(methods.tuple("floor"));
        int[] td = getTileDimensions(0, 0, tileSize, tileSize, screenX, screenY, screenTileW, screenTileH);
        g.drawImage(tileSheet, td[4], td[5], td[6], td[7], 0, 0, tileSize, tileSize, null);
        //g.drawImage(tileSheet, screenX - 1, screenY, screenX + screenTileW + 1, screenY + screenTileH, 0, 0, tileSize, tileSize, null);
    }

    //Draw a tile at a specified location.
    public void drawTile(Graphics g, int x, int y, int sx, int sy, int tw, int th, Enums.tileType tile, Color colour) {
        try {
            if (map.map[y][x] == tile) {
                g.setColor(colour);
                drawWall(g, x, y, sx, sy, tw, th);
                g.setColor(BLACK);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    public void drawTile(Graphics g, int x, int y, int ix, int iy, int tw, int th, Color colour) {
        drawTile(g, x, y, ix, iy, tw, th, 1, colour);
    }
    public int drawTile(Graphics g, int x, int y, int ix, int iy, int tw, int th, double scale, Color colour) {
        int border = ((int) (tileSize * scale) - tileSize) / 2;
        try {
            g.drawImage(tileSheet, x - 1 - border, y - border, x + tw + border + 1, y + th + border, ix, iy, ix + tw, iy + th, null);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            g.setColor(colour);
            g.fillRect(x, y, tw, th);
            g.setColor(BLACK);
        }
        return border;
    }

    public int[] getTileDimensions(int tileX, int tileY, int tileW, int tileH, int screenX, int screenY, int screenTileW, int screenTileH) {
        int sx1 = tileX, sy1 = tileY, sx2 = tileX + tileW, sy2 = tileY + tileH;
        int dx1 = screenX - 1, dy1 = screenY, dx2 = screenX + screenTileW + 1, dy2 = screenY + screenTileH;
        //System.out.println("1 " + methods.tuple("tile", sx1, sy1, sx2, sy2, "screen", dx1, dy1, dx2, dy2));
        if (screenX < 0) {
            sx1 = sx1 + (screenX / screenTileW) * tileW;
            dx1 = 0;
            dx2 = dx2 - (screenX + screenTileW);
            //System.out.println("  2 " + methods.tuple("X", screenX, "source", sx1, sx2, "dest", dx1, dx2));
        }
        if (screenY < 0) {
            sy1 = sy1 + (screenY / screenTileH) * tileH;
            dy1 = 0;
            dy2 = dy2 - (screenY + screenTileH);
            //System.out.println("  2 " + methods.tuple("Y", screenY, "source", sy1, sy2, "dest", dy1, dy2));
        }
        return new int[] {sx1, sy1, sx2, sy2, dx1, dy1, dx2, dy2};
    }

    //Draw the room's wall tiles using the map's wallChecker's autotiling functionality.
    public void drawRoomTiles(Graphics g, int tileX, int tileY, int screenX, int screenY, int screenTileW, int screenTileH, Enums.tileType tile, Color colour) {
        if (tileSheet != null) {
            if (map.isWall(tileX, tileY)) {
                //Top, Bottom, Left, Right, Top-Left, Top-Right, Bottom-Left, Bottom-Right
                int[] tilePos;
                String code = null;
                if (autotiled) {
                    code = map.wallChecker.getAdjacentTiles(map.map, tileX, tileY);
                    tilePos = tileMap.get(code);
                } else {
                    tilePos = tileGrid[tileY][tileX];
                }
                //System.out.println(methods.tuple("wall", autotiled, tileX, tileY, tilePos, code));
                if (tilePos != null) {
                    int[] td = getTileDimensions(tilePos[0] * tileSize, tilePos[1] * tileSize, tileSize, tileSize, screenX, screenY, screenTileW, screenTileH);
                    g.drawImage(tileSheet, td[4], td[5], td[6], td[7], td[0], td[1], td[2], td[3], null);
                } else {
                    drawTile(g, tileX, tileY, screenX, screenY, screenTileW, screenTileH, tile, colour);
                }
            }
        }
        else if (background != null) {
            g.drawImage(background, 0, 0, map.camera.getWidth(), map.camera.getHeight(), map.camera.getX(), map.camera.getY(), map.camera.getWidth(), map.camera.getHeight(), null);
        }
    }
}
