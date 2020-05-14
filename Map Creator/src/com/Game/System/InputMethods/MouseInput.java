package com.Game.System.InputMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {
    JComponent frame;
    int mx = 0;
    int my = 0;
    int mb;
    String mt;
    boolean pushed = false;
    public MouseInput() {}
    public MouseInput(JComponent frame) {
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

    public void setMouseLocation(MouseEvent e, String type) {
        mx = e.getX();
        my = e.getY();
        mb = e.getButton();
        mt = type;
        //System.out.println(methods.tuple(mt.toUpperCase(), mb, pushed, mx, my));
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        setMouseLocation(e, "clicked");
    }
    @Override
    public void mousePressed(MouseEvent e) {
        setMouseLocation(e, "pressed");
        pushed = true;
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
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        setMouseLocation(e, "moved");
    }
}
