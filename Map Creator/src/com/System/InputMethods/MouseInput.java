package com.System.InputMethods;

import com.Entities.Characters.CharacterModel;
import com.GUI.GUI;
import com.System.Enums;
import com.methods;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

import static java.awt.Color.RED;
import static java.awt.event.MouseEvent.*;

public class MouseInput implements MouseInputListener {
    GUI frame;
    int mx;
    int my;
    public MouseInput(GUI frame) {
        this.frame = frame;
    }

    public Point getMouseLocation() {
        return new Point(mx, my);
    }
    public void setMouseLocation(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        System.out.println(methods.tuple("CLICK", e.getButton(), mx, my));
    }
    public void mouseClicked(MouseEvent e) {
        setMouseLocation(e);
        switch (e.getButton()) {
            case BUTTON1:
                for (CharacterModel character : frame.camera.getMap().characterList) {
                    Point coords = frame.camera.getRelativeCoordinates(character.x * frame.camera.tileSize, character.y * frame.camera.tileSize);
                    if ((new Rectangle(coords.x, coords.y, frame.camera.tileSize, frame.camera.tileSize)).contains(mx, my)) {
                        frame.hud.select(character);
                    }
                } break;
            case BUTTON3: frame.hud.select(null); break;
        }
        System.out.println(frame.camera.selected);
    }
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseDragged(MouseEvent e) {

    }
    public void mouseMoved(MouseEvent e) {
        setMouseLocation(e);
    }
}
