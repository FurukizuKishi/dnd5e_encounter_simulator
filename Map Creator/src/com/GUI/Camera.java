package com.GUI;

import com.Entities.Characters.CharacterModel;
import com.Entities.Entity;
import com.Map.Map;
import com.System.Enums;
import com.System.Text.FloatingText;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;

import static java.awt.Color.RED;

public class Camera extends JPanel {
    public int w, h, sw, sh;                    //Camera's tile dimensions and the screen's dimensions.
    int cx, cy; double px, py;                  //The current screen coordinates being accessed.
    int x, y;                                   //The current x and y position of the camera.
    int mSpeed = 8;                             //The camera's movement speed.
    double slowdown = 0.01;                     //The camera's slowdown fraction.
    public int tileSize;                        //The size of tiles in the game.
    public Entity selected;                     //The object the camera is following.
    public int titleThickness = 32;             //The size of the window's title bar.
    GUI frame;                                  //The camera's parent frame.
    Map map;                                    //The map the camera is currently in.
    HUD hud;                                    //The HUD the camera is using.
    Color borderColour = new Color(255, 255, 255);
    Color fillColour = new Color(0, 135, 0);
    Color backColour = new Color(45, 45, 45);

    //Camera constructor.
    public Camera(GUI frame, int w, int h, HUD hud, Map map) {
        super();
        this.frame = frame;
        this.w = w;
        this.h = h;
        this.sw = hud.sw;
        this.sh = hud.sh;
        this.tileSize = Math.min(this.sw / this.w, this.sh / this.h);
        this.sw = Math.min(this.sw, this.tileSize * this.w);
        this.sh = Math.min(this.sh, this.tileSize * this.h);
        hud.camera = this;
        this.selected = hud.selected;
        this.map = map;
        this.hud = hud;
    }

    //The camera draws all visual elements onto the screen. All objects with a Graphics function offload the painting to the camera.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (map != null) {
            paintRoom((Graphics2D) g);
            hud.paintHUD(g, Enums.alignmentHorizontal.LEFT, fillColour, backColour, borderColour);
        }
        for (int i = 0; i < frame.transitions.size(); i += 1) {
            frame.transitions.get(i).paintTransition(g);
        }
        repaint();
    }

    //Change the camera's current map. Alone this won't work. For a full removal, the com.GUI's unlinkRoom() function will need to be called.
    public void setMap(Map map) {
        this.map = map;
        if (this.map != null) {
            map.camera = this;
        }
    }

    //Fetch the camera's current map.
    public Map getMap() {
        return map;
    }

    //Calculate the camera's current coordinates in the map and thus determine which part of the map to draw.
    public void calculateCoordinates(int x, int y) {
        double sx = this.x / (double) tileSize;
        double sy = this.y / (double) tileSize;
        px = x + sx - (w / 2.0);
        py = y + sy - (h / 2.0);
        cx = methods.integerDivision(x, w, sw);
        cy = methods.integerDivision(y, h, sh);
    }

    //Get the camera's bounding box.
    public Rectangle getCameraBox() {
        int x1 = x - ((w / 2) * tileSize);
        int y1 = y - ((h / 2) * tileSize);
        return new Rectangle(x1, y1, w * tileSize, h * tileSize);
    }

    //Get an object's relative coordinates to the camera.
    public Point getRelativeCoordinates(int x, int y) {
        Rectangle cameraBox = getCameraBox();
        Rectangle tile = new Rectangle(x, y, tileSize, tileSize);
        if (cameraBox.intersects(tile)) {
            return new Point(Math.max(0, x - cameraBox.x), Math.max(0, y - cameraBox.y));
        }
        return null;
    }

    //Get an object's relative coordinates to the camera.
    public Point getAbsoluteCoordinates(int x, int y) {
        Rectangle cameraBox = getCameraBox();
        return new Point(Math.min(x + cameraBox.x, map.w * tileSize - 1), Math.min(y + cameraBox.y, map.h * tileSize - 1));
    }

    //Get an object's position in the window, or null if it outside of it.
    public Point getWindowPosition(Point p) {
        return getWindowPosition(p.x, p.y);
    }
    //Get an object's position in the window, or null if it outside of it.
    public Point getWindowPosition(int x, int y) {
        Rectangle window = getWindowBox();
        if (window.contains(x, y)) {
            return new Point(x + window.x, y + window.y);
        }
        return null;
    }

    //Get the window's bounding box.
    public Rectangle getWindowBox() {
        return new Rectangle(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
    }

    //Get the border region of the window at which point the mouse can pull the camera from.
    public Area getDraggableRegion(int border) {
        Area window = new Area(getWindowBox());
        Rectangle bounds = window.getBounds();
        Area exclusion = new Area(new Rectangle((int) bounds.getX() + border, (int) bounds.getY() + border, (int) bounds.getWidth() - (border * 2), (int) bounds.getHeight() - (border * 2)));
        window.exclusiveOr(exclusion);
        return window;
    }

    //Get the mouse position.
    public Point getMouseScreenPosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    //Get the mouse in the map.
    public Point getMouseMapPosition() {
        Point pos = getWindowPosition(getMouseScreenPosition());
        if (pos != null) {
            return getAbsoluteCoordinates(pos.x, pos.y);
        }
        return null;
    }

    //Move towards the mouse if nothing is selected.
    public void moveTowardsMouse() {
        Point target = getMouseMapPosition();
        if (selected != null) {
            target = new Point(selected.x * tileSize, selected.y * tileSize);
        }
        if (target != null) {
            x = (int) Math.max((w / 2.0) * tileSize, Math.min(x + (target.x - x) * slowdown, (map.w - (w / 2.0)) * tileSize));
            y = (int) Math.max((h / 2.0) * tileSize, Math.min(y + (target.y - y) * slowdown, (map.h - (h / 2.0)) * tileSize));
        }
    }

    //Paint the room the camera is in, drawing its tiles using the Background class' autotiling functionality. The separate for loops draw the tiles
    //in layers, making sure everything is properly positioned in the z-axis.
    public void paintRoom(Graphics2D g) {
        Point mouse = getMouseScreenPosition();
        if (mouse != null) {
            Area pull = getDraggableRegion(64);
            Point focus = getWindowPosition(mouse);
            if (focus != null) {
                if (pull.contains(focus)) {
                    moveTowardsMouse();
                }
            }
        }
        int cBorder = 3;
        //Draw floor.
        for (int y = -cBorder; y < map.h + cBorder; y += 1) {
            for (int x = -cBorder; x < map.w + cBorder; x += 1) {
                calculateCoordinates(x, y);
                Point coords = getRelativeCoordinates(x * tileSize, y * tileSize);
                if (coords != null) {
                    map.background.drawRoomFloor(g, x, y, coords.x, coords.y - titleThickness, tileSize);
                }
            }
        }
        //Draw water.
        for (int y = -cBorder; y < map.h + cBorder; y += 1) {
            for (int x = -cBorder; x < map.w + cBorder; x += 1) {
                calculateCoordinates(x, y);
                Point coords = getRelativeCoordinates(x * tileSize, y * tileSize);
                if (coords != null) {
                    map.background.drawRoomTiles(g, x, y, coords.x, coords.y - titleThickness, tileSize, Enums.tileType.WATER, new Color(55, 55,255));
                }
            }
        }
        //Draw walls.
        for (int y = -cBorder; y < map.h + cBorder; y += 1) {
            for (int x = -cBorder; x < map.w + cBorder; x += 1) {
                calculateCoordinates(x, y);
                Point coords = getRelativeCoordinates(x * tileSize, y * tileSize);
                if (coords != null) {
                    map.background.drawRoomTiles(g, x, y, coords.x, coords.y - titleThickness, tileSize, Enums.tileType.WALL, new Color(0, 0, 0));
                }
            }
        }
        //Draw movement path tiles.
        for (CharacterModel character : map.characterList) {
            if (character.pathfinder.isActive()) {
                for (int y = -cBorder; y < map.h + cBorder; y += 1) {
                    for (int x = -cBorder; x < map.w + cBorder; x += 1) {
                        Point bigCoords = getRelativeCoordinates(x * tileSize, y * tileSize);
                        if (bigCoords != null) {
                            Point absCoords = getAbsoluteCoordinates(bigCoords.x, bigCoords.y);
                            Point coords = new Point(absCoords.x / tileSize, absCoords.y / tileSize);
                            Enums.pathTile node = character.pathfinder.pathGrid[coords.y][coords.x];
                            if (node != Enums.pathTile.NULL) {
                                Color colour;
                                switch (node) {
                                    case ATTACK:
                                        colour = Color.RED;
                                        break;
                                    default:
                                        colour = Color.BLUE;
                                        break;
                                }
                                g.setColor(colour);
                                g.fillRect(bigCoords.x + 1, bigCoords.y - titleThickness + 1, tileSize - 3, tileSize - 2);
                            }
                        }
                    }
                }
            }
        }
        //Draw characters.
        Rectangle cBox = getCameraBox();
        for (int y = -cBorder; y < map.h + cBorder; y += 1) {
            for (int x = -cBorder; x < map.w + cBorder; x += 1) {
                calculateCoordinates(x, y);
                for (CharacterModel character : map.characterList) {
                    if (x == character.x && y == character.y) {
                        Point coords = getRelativeCoordinates(x * tileSize, y * tileSize);
                        if (coords != null) {
                            character.drawSelf(g, coords.x, coords.y - titleThickness, tileSize, RED);
                            hud.paintHealthbar(g, Enums.alignmentHorizontal.LEFT, coords.x, coords.y - titleThickness, character, fillColour, backColour, borderColour);
                        }
                    }
                }
            }
        }
        //Draw floating text.
        for (FloatingText text : frame.textStores.get("damage").floatingText) {
            frame.alignFloatingText(text, Enums.alignmentHorizontal.MIDDLE);
            if (cBox.contains(text.x * tileSize, text.y * tileSize)) {
                int textX = text.x - (int) cBox.getBounds().getX();
                int textY = text.y - (int) cBox.getBounds().getY();
                Point coords = getRelativeCoordinates((int) (textX + 0.5) * tileSize, (int) (textY + 0.25) * tileSize);
                text.display(g, coords.x, coords.y - titleThickness, tileSize);
            }
        }
    }
}