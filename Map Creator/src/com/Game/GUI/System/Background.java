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
            setTilesheet(ImageIO.read(new File(backgroundPath + ".png")), tileSize);
            initializeTileMap();
        }
        catch (Exception e) {
            //System.out.println("[ERR]: " + e);
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
    public void drawWall(Graphics g, int tileX, int tileY, int screenX, int screenY, int screenTileSize) {
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
            g.fillRect(screenX - 1, screenY, screenTileSize + 2, screenTileSize);
            if (map.canEdit()) {
                methods.drawString(g, text, screenX - 1 + (screenTileSize / 2), screenY + (screenTileSize / 2), Fonts.font.TEXT, WHITE, 4);
            }
        }
    }

    //Draw the repeating pattern for the room's floor.
    public void drawRoomFloor(Graphics g, int tileX, int tileY, int screenX, int screenY, int screenTileSize) {
        g.drawImage(tileSheet, screenX - 1, screenY, screenX + screenTileSize + 1, screenY + screenTileSize, 0, 0, tileSize, tileSize, null);
    }

    //Draw a tile at a specified location.
    public void drawTile(Graphics g, int x, int y, int sx, int sy, int ts, Enums.tileType tile, Color colour) {
        try {
            if (map.map[y][x] == tile) {
                g.setColor(colour);
                drawWall(g, x, y, sx, sy, ts);
                g.setColor(BLACK);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    public void drawTile(Graphics g, int x, int y, int ix, int iy, Color colour) {
        drawTile(g, x, y, ix, iy, 1, colour);
    }
    public int drawTile(Graphics g, int x, int y, int ix, int iy, double scale, Color colour) {
        int border = ((int) (tileSize * scale) - tileSize) / 2;
        try {
            g.drawImage(tileSheet, x - 1 - border, y - border, x + tileSize + border + 1, y + tileSize + border, ix, iy, ix + tileSize, iy + tileSize, null);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            g.setColor(colour);
            g.fillRect(x, y, tileSize, tileSize);
            g.setColor(BLACK);
        }
        return border;
    }

    //Draw the room's wall tiles using the map's wallChecker's autotiling functionality.
    public void drawRoomTiles(Graphics g, int tileX, int tileY, int screenX, int screenY, int screenTileSize, Enums.tileType tile, Color colour) {
        if (tileSheet != null) {
            //Top, Bottom, Left, Right, Top-Left, Top-Right, Bottom-Left, Bottom-Right
            int[] tilePos;
            if (autotiled) {
                tilePos = tileMap.get(map.wallChecker.getAdjacentTiles(map.map, tileX, tileY));
            }
            else {
                tilePos = tileGrid[tileY][tileX];
            }
            if (tilePos != null) {
                int sheetX = tilePos[0] * tileSize;
                int sheetY = tilePos[1] * tileSize;
                //com.Game.System.out.println(methods.tuple(sheetX, sheetY));
                int sx1 = sheetX, sy1 = sheetY, sx2 = sheetX + tileSize, sy2 = sheetY + tileSize;
                int dx1 = screenX - 1, dy1 = screenY, dx2 = screenX + screenTileSize + 1, dy2 = screenY + screenTileSize;
                if (screenX < 0) {
                    sx1 = sx1 - screenX;
                    dx2 = dx2 + screenX;
                }
                if (screenY < 0) {
                    sy1 = sy1 - screenY;
                    dy2 = dy2 + screenY;
                }
                g.drawImage(tileSheet, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
            }
            else {
                drawTile(g, tileX, tileY, screenX, screenY, screenTileSize, tile, colour);
            }
        }
        else if (background != null) {
            g.drawImage(background, 0, 0, map.camera.getWidth(), map.camera.getHeight(), map.camera.getX(), map.camera.getY(), map.camera.getWidth(), map.camera.getHeight(), null);
        }
    }
}
