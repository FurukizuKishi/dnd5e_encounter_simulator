package com.GUI;

import com.Entities.Characters.Character;
import com.Entities.Entity;
import com.Map.Map;
import com.System.Enums;
import com.methods;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.RED;

public class Camera extends JPanel {
    public int w, h, sw, sh;                    //Camera's tile dimensions and the screen's dimensions.
    int cx, cy, px, py;                         //The current screen coordinates being accessed.
    int tileSize;                               //The size of tiles in the game.
    Entity selected;                            //The player the camera is following.
    GUI frame;                                  //The camera's parent frame.
    Map map;                                    //The map the camera is currently in.
    HUD hud;                                    //The HUD the camera is using.

    //Camera constructor.
    public Camera(GUI frame, int w, int h, Map map) {
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
        this.selected = hud.player;
        this.map = map;
        this.hud = hud;
    }

    //The camera draws all visual elements onto the screen. All objects with a Graphics function offload the painting to the camera.
    public void paint(Graphics g) {
        if (map != null) {
            paintRoom(g);
            hud.paintHUD(g);
        }
        for (int i = 0; i < frame.transitions.size(); i += 1) {
            frame.transitions.get(i).paintTransition(g);
        }
        if (frame.roomTransition != null) {
            if (frame.roomTransition.active) {
                frame.roomTransition.paintTransition(g);
                if (frame.roomTransition != null) {
                    if (frame.roomTransition.levelEnd != null) {
                        frame.roomTransition.levelEnd.paintSelf(g);
                    }
                }
            }
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

    //Calculate the camera's current coordinates in the map and thus determine which part of the map to draw.
    public void calculateCoordinates(int x, int y) {
        px = x + selected.x - (w / 2);
        py = y + selected.y - (h / 2);
        cx = methods.integerDivision(x, w, sw);
        cy = methods.integerDivision(y, h, sh);
    }

    //Get the camera's bounding box.
    public Rectangle getCameraBox() {
        int x1 = selected.x - (w / 2);
        int y1 = selected.y - (h / 2);
        return new Rectangle(x1, y1, w, h);
    }

    //Paint the room the camera is in, drawing its tiles using the Background class' autotiling functionality. The separate for loops draw the tiles
    //in layers, making sure everything is properly positioned in the z-axis.
    public void paintRoom(Graphics g) {
        for (int y = 0; y < h; y += 1) {
            for (int x = 0; x < w; x += 1) {
                calculateCoordinates(x, y);
                map.background.drawRoomFloor(g, px, py, cx, cy, tileSize);
            }
        }
        for (int y = 0; y < h; y += 1) {
            for (int x = 0; x < w; x += 1) {
                calculateCoordinates(x, y);
                map.background.drawRoomTiles(g, px, py, cx, cy, tileSize);
            }
        }
        for (int y = 0; y < h; y += 1) {
            for (int x = 0; x < w; x += 1) {
                calculateCoordinates(x, y);
                map.background.drawRoomDoors(g, px, py, cx, cy, tileSize);
            }
        }
        Rectangle cBox = getCameraBox();
        for (int y = 0; y < h; y += 1) {
            for (int x = 0; x < w; x += 1) {
                calculateCoordinates(x, y);
                for (Character character : map.characterList) {
                    if (px == character.x && py == character.y) {
                        character.drawSelf(g, cx, cy, tileSize, RED);
                        hud.paintHealthbars(g, cx, cy, character);
                    }
                }
            }
        }
        for (FloatingText text : frame.textStores.get("damage").floatingText) {
            frame.alignFloatingText(text, Enums.alignment.MIDDLE);
            if (cBox.contains(text.x, text.y)) {
                int textX = text.x - (int) cBox.getBounds().getX();
                int textY = text.y - (int) cBox.getBounds().getY();
                text.display(g, textX + 0.5, textY + 0.25, tileSize);
            }
        }
    }
}