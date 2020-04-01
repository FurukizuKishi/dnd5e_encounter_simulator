package com.Entities;

import com.methods;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public String spriteBaseFolder = "sprites/";                            //Attributes regarding entity sprites.
    public String spritePath;
    public BufferedImage spriteSheet;
    public String name;
    public int x;                                                           //The entity's coordinates.
    public int y;
    public Rectangle boundingBox;                                           //The entity's bounding box.

    //Fetch the entity's sprites for different actions. Sprite filenames are determined by the entity's name.
    public void getSprite() {
        this.spritePath += name;
        this.spriteSheet = methods.getSprite(this.spritePath);
        System.out.println(this.spriteSheet);
        this.spritePath += ".png";
        System.out.println("[DBG]: " + name + " sprite - " + this.spritePath);
    }

    //Update the bounding box.
    public void setBoundingBox() {
        if (boundingBox == null) {
            boundingBox = new Rectangle(x, y, 1, 1);
        }
        boundingBox.setLocation(x, y);
    }
}