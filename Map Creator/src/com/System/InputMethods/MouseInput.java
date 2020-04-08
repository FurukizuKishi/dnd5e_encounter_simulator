package com.System.InputMethods;

import com.Entities.Characters.CharacterModel;
import com.GUI.GUI;
import com.System.Enums;
import com.methods;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static java.awt.event.MouseEvent.*;

public class MouseInput implements MouseListener, MouseMotionListener {
    GUI frame;
    int mx;
    int my;
    int mb;
    boolean pushed = false;
    public MouseInput(GUI frame) {
        this.frame = frame;
    }

    public Point getMouseLocation() {
        return new Point(mx, my);
    }
    public Point getMouseLocation(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }
    public boolean getPressedDown() {
        return pushed;
    }

    public void selectPathTile() {
        if (mb == BUTTON1) {
            CharacterModel character = frame.hud.getSelected();
            if (character != null) {
                Point bigCoords = frame.camera.getRelativeCoordinates(mx, my);
                Point coords = new Point(bigCoords.x / frame.camera.tileSize, bigCoords.y / frame.camera.tileSize);
                boolean canSelect = false;
                if (character.pathfinder.pathGrid[coords.y][coords.x] == Enums.pathTile.FREE) {
                    if (character.pathfinder.path.size() < character.pathfinder.getMove()) {
                        if (character.pathfinder.path.size() > 0) {
                            int[] point = character.pathfinder.path.get(character.pathfinder.path.size() - 1);
                            if (character.pathfinder.adjacentCells(point[0], point[1], coords.x, coords.y)) {
                                canSelect = true;
                            }
                        } else {
                            if (character.pathfinder.adjacentCells(character.x, character.y, coords.x, coords.y)) {
                                canSelect = true;
                            }
                        }
                    }
                }
                if (canSelect) {
                    character.pathfinder.path.add(new int[]{coords.x, coords.y});
                }
            }
        }
    }

    public void setMouseLocation(MouseEvent e, String type) {
        mx = e.getX();
        my = e.getY();
        mb = e.getButton();
        System.out.println(methods.tuple(type.toUpperCase(), mb, pushed, mx, my));
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        setMouseLocation(e, "clicked");
        switch (mb) {
            case BUTTON1:
                for (CharacterModel character : frame.camera.getMap().characterList) {
                    Point coords = frame.camera.getRelativeCoordinates(character.x * frame.camera.tileSize, character.y * frame.camera.tileSize);
                    if (coords != null) {
                        if ((new Rectangle(coords.x, coords.y, frame.camera.tileSize, frame.camera.tileSize)).contains(mx, my)) {
                            frame.hud.select(character);
                        }
                    }
                } break;
            case BUTTON3: frame.hud.select(null); break;
        }
        System.out.println(frame.camera.selected);
    }
    @Override
    public void mousePressed(MouseEvent e) {
        setMouseLocation(e, "pressed");
        pushed = true;
        selectPathTile();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        setMouseLocation(e, "released");
        pushed = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        setMouseLocation(e, "entered");
    }
    @Override
    public void mouseExited(MouseEvent e) {
        setMouseLocation(e, "exited");
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        setMouseLocation(e, "dragged");
        pushed = true;
        selectPathTile();
    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
