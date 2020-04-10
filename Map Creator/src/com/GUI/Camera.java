package com.GUI;

import com.Entities.Characters.CharacterModel;
import com.Map.Map;
import com.System.Enums;
import com.System.InputMethods.MouseInput;
import com.System.Text.FloatingText;
import com.globals;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;

import static java.awt.Color.*;

public class Camera extends JPanel {
    public int w, h, sw, sh;                    //Camera's tile dimensions and the screen's dimensions.
    int cx, cy; double px, py;                  //The current screen coordinates being accessed.
    int x, y;                                   //The current x and y position of the camera.
    int mSpeed = 8;                             //The camera's movement speed.
    double slowdown = 0.01;                     //The camera's slowdown fraction.
    public int tileSize;                        //The size of tiles in the game.
    public CharacterModel selected;             //The object the camera is following.
    public int titleThickness = 32;             //The size of the window's title bar.
    GUI frame;                                  //The camera's parent frame.
    Map map;                                    //The map the camera is currently in.
    HUD hud;                                    //The HUD the camera is using.

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
    public Point getRelativeCoordinates(Point p) {
        return getRelativeCoordinates(p.x, p.y);
    }
    public Point getRelativeCoordinates(int x, int y) {
        Rectangle cameraBox = getCameraBox();
        Rectangle tile = new Rectangle(x, y, tileSize, tileSize);
        if (cameraBox.intersects(tile)) {
            return new Point(Math.max(0, x - cameraBox.x), Math.max(0, y - cameraBox.y));
        }
        return null;
    }

    //Get an object's relative coordinates to the camera.
    public Point getAbsoluteCoordinates(Point p) {
        return getAbsoluteCoordinates(p.x, p.y);
    }
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

    public boolean inDraggableRegion(int border, Point p) {
        return inDraggableRegion(border, p.x, p.y);
    }
    public boolean inDraggableRegion(int border, int x, int y) {
        if (getDraggableRegion(border).contains(x, y)) {
            return true;
        }
        return false;
    }

    //Get the border region of the window at which point the mouse can pull the camera from.
    public Area getDraggableRegion(int border) {
        Area window = new Area(getWindowBox());
        Rectangle bounds = window.getBounds();
        Area shape = new Area(new Rectangle(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight() - titleThickness));
        Area exclusion = new Area(new Rectangle(border, border, (int) bounds.getWidth() - (border * 2), (int) bounds.getHeight() - titleThickness - (border * 2)));
        //Area shape = new Area(new Rectangle((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight() - titleThickness));
        //Area exclusion = new Area(new Rectangle((int) bounds.getX() + border, (int) bounds.getY() + border, (int) bounds.getWidth() - (border * 2), (int) bounds.getHeight() - titleThickness - (border * 2)));
        shape.exclusiveOr(exclusion);
        bounds = shape.getBounds();
        //System.out.println(methods.tuple(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()));
        return shape;
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

    //Check if the mouse is being dragged.
    public boolean getMouseDrag() {
        Point mouse = getMouseScreenPosition();
        MouseInput input = (MouseInput) frame.getMouseMotionListeners()[0];
        if (input.getPressedDown()) {
            if (!(input.getMouseLocation().x == mouse.x && input.getMouseLocation().y == mouse.y)) {
                return true;
            }
        }
        return false;
    }

    //Move towards the mouse if nothing is selected.
    public void moveTowardsPoint(int x, int y) {
        this.x = (int) Math.max((w / 2.0) * tileSize, Math.min(this.x + (x - this.x) * slowdown, (map.w - (w / 2.0)) * tileSize));
        this.y = (int) Math.max((h / 2.0) * tileSize, Math.min(this.y + (y - this.y) * slowdown, (map.h - (h / 2.0)) * tileSize));
    }

    //The camera draws all visual elements onto the screen. All objects with a Graphics function offload the painting to the camera.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (map != null) {
            paintRoom((Graphics2D) g);
            hud.paintHUD(g, Enums.alignmentHorizontal.LEFT, globals.healthFillColour, globals.moveFillColour, globals.healthBackColour, globals.moveBackColour, globals.borderColour);
            if (selected != null) {
                System.out.println(methods.tuple("selected", selected, selected.actor.moving, selected.actor.node));
            }
            else {
                System.out.println(methods.tuple("selected", null));
            }
        }
        for (int i = 0; i < frame.transitions.size(); i += 1) {
            frame.transitions.get(i).paintTransition(g);
        }
        repaint();
    }

    //Paint the room the camera is in, drawing its tiles using the Background class' autotiling functionality. The separate for loops draw the tiles
    //in layers, making sure everything is properly positioned in the z-axis.
    public void paintRoom(Graphics2D g) {
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
                            if (methods.containsCoordinate(character.pathfinder.path, coords.x, coords.y) == -1) {
                                Enums.pathTile node = character.pathfinder.pathGrid[coords.y][coords.x];
                                if (!(node == Enums.pathTile.NULL || node == Enums.pathTile.ATTACK)) {
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
        }
        //Get the camera's bounding box.
        Rectangle cBox = getCameraBox();
        //Draw characters.
        for (int y = -cBorder; y < map.h + cBorder; y += 1) {
            for (int x = -cBorder; x < map.w + cBorder; x += 1) {
                calculateCoordinates(x, y);
                for (CharacterModel character : map.characterList) {
                    if (x == character.x && y == character.y) {
                        Point coords = getRelativeCoordinates(x * tileSize, y * tileSize);
                        if (coords != null) {
                            character.drawSelf(g, coords.x, coords.y - titleThickness, tileSize, RED);
                            hud.paintHealthbar(g, Enums.alignmentHorizontal.LEFT, coords.x, coords.y - titleThickness, character, globals.healthFillColour, globals.healthBackColour, globals.borderColour);
                        }
                    }
                }
            }
        }
        //Draw a selected character's path.
        for (int y = -cBorder; y < map.h + cBorder; y += 1) {
            for (int x = -cBorder; x < map.w + cBorder; x += 1) {
                calculateCoordinates(x, y);
                for (CharacterModel character : map.characterList) {
                    if (character.pathfinder.isActive()) {
                        for (int[] node : character.pathfinder.path) {
                            if (x == node[0] && y == node[1]) {
                                Point coords = getRelativeCoordinates(x * tileSize, y * tileSize);
                                if (coords != null) {
                                    g.setColor(Color.GREEN);
                                    g.fillRect(coords.x + 1, coords.y - titleThickness + 1, tileSize - 3, tileSize - 2);
                                }
                            }
                        }
                    }
                }
            }
        }
        //Draw floating text.
        for (FloatingText text : frame.textStores.get("damage").floatingText) {
            methods.alignFloatingText(g, text, Enums.alignmentHorizontal.MIDDLE);
            if (cBox.contains(text.x * tileSize, text.y * tileSize)) {
                int textX = text.x - (int) cBox.getBounds().getX();
                int textY = text.y - (int) cBox.getBounds().getY();
                Point coords = getRelativeCoordinates((int) (textX + 0.5) * tileSize, (int) (textY + 0.25) * tileSize);
                text.display(g, coords.x, coords.y - titleThickness, tileSize);
            }
        }
    }
}