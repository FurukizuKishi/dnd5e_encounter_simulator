package com.Entities;

import com.GUI.GUI;
import com.methods;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public GUI frame;                                                       //The entity's graphical game frame.
    public String spritePath;
    public BufferedImage sprite;
    public String name;                                                     //The entity's name.
    public int x;                                                           //The entity's coordinates.
    public int y;
    public double betweenX;                                                 //The distance an entity is between x-coordinates.
    public double betweenY;                                                 //The distance an entity is between y-coordinates.
    public Rectangle boundingBox;                                           //The entity's bounding box.

    //Fetch the entity's sprites for different actions.
    public void setSprite(String spritePath) {
        this.spritePath = spritePath;
        this.sprite = methods.getExternalImage(this.spritePath);
        System.out.println("[DBG]: " + this.sprite + " - " + this.spritePath);
    }

    //Set the entity's gui frame.
    public void setFrame(GUI frame) {
        this.frame = frame;
    }

    //Update the bounding box.
    public void setBoundingBox() {
        if (boundingBox == null) {
            boundingBox = new Rectangle(x, y, 1, 1);
        }
        boundingBox.setLocation(x, y);
    }
}