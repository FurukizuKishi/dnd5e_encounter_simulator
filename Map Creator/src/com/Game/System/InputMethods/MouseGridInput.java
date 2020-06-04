package com.Game.System.InputMethods;

import com.Game.Entities.Characters.CharacterModel;
import com.Game.GUI.GUI;
import com.Game.System.Enums;

import java.awt.*;
import java.awt.event.MouseEvent;

import static java.awt.event.MouseEvent.*;

public class MouseGridInput extends MouseInput {
    GUI frame;
    int mx = 0;
    int my = 0;
    int mb;
    String mt;
    boolean pushed = false;
    public MouseGridInput(GUI frame) {
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

    public void moveCamera() {
        Point mouse = getMouseLocation();
        Point target = null;
        if (frame.camera.inDraggableRegion(64, mouse)) {
            target = frame.camera.getAbsoluteCoordinates(mouse);
        }
        if (frame.camera.selected != null) {
            target = new Point(frame.camera.selected.x * frame.camera.tileSize, frame.camera.selected.y * frame.camera.tileSize);
        }
        if (target != null) {
            frame.camera.moveTowardsPoint(target.x, target.y);
        }
    }

    public void selectPathTile() {
        if (mb == BUTTON1 || mt.equals("dragged")) {
            CharacterModel character = frame.hud.getSelected();
            if (character != null) {
                Point bigCoords = frame.camera.getAbsoluteCoordinates(mx, my);
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
        mt = type;
        //System.out.println(methods.tuple(mt.toUpperCase(), mb, pushed, mx, my));
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        switch (mb) {
            case BUTTON1:
                for (CharacterModel character : frame.camera.getMap().characterList) {
                    Point coords = frame.camera.getRelativeCoordinates(character.x * frame.camera.tileSize, character.y * frame.camera.tileSize);
                    if (coords != null) {
                        if ((new Rectangle(coords.x, coords.y, frame.camera.tileSize, frame.camera.tileSize)).contains(mx, my)) {
                            //System.out.println(character);
                            if (!frame.hud.select(character)) {
                                character.charSheet.display();
                            }
                        }
                    }
                } break;
            case BUTTON2:
                for (CharacterModel character : frame.camera.getMap().characterList) {
                    Point coords = frame.camera.getRelativeCoordinates(character.x * frame.camera.tileSize, character.y * frame.camera.tileSize);
                    if (coords != null) {
                        if ((new Rectangle(coords.x, coords.y, frame.camera.tileSize, frame.camera.tileSize)).contains(mx, my)) {
                            //System.out.println(character);
                            if (character.pathfinder.path.size() > 0) {
                                character.actor.move();
                            }
                        }
                    }
                } break;
            case BUTTON3:
                frame.hud.select(null); break;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        selectPathTile();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        selectPathTile();
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }
}
