package com.Entities.Characters;

import com.Entities.Conditions.Condition;
import com.Entities.Entity;
import com.Map.Map;
import com.System.Enums;
import com.methods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Character extends Entity {
    public String name;                                                     //The character's name.
    public int tileSize = 32;                                               //The size of tiles in the game.
    public boolean attacking = false;                                       //Whether the character is attacking.
    public int attackTick = 0;                                              //The timer for the character's attack.
    public int attackTime = 0;
    public boolean invulnerable = false;                                    //Whether the character is flashing white.
    public CharacterSheet charSheet = new CharacterSheet();
    public int x;                                                           //The character's coordinates.
    public int y;
    public Rectangle boundingBox;                                           //The character's bounding box.
    public int xDir;                                                        //The character's direction.
    public int yDir;
    public int space;                                                       //How much space the character takes up.
    public Map map;                                                         //What map the character is currently in.
    public ArrayList<Condition> conditions = new ArrayList<>();             //All of the conditions the character has applied to them.

    public void checkHealth() {

    }

    public void damageHealth(int damage) {
        //invulnerable = true;
        charSheet.damageHealth(damage);
        map.frame.textStores.get("damage").addDamageText(damage, x, y);
        checkHealth();
    }

    public void damageHealth(int damage, Color damageTextColour) {
        damageHealth(damage);
        ArrayList<FloatingText> textStore = map.frame.textStores.get("damage").floatingText;
        textStore.get(textStore.size() - 1).setColours(damageTextColour);
    }

    public boolean isAfflicted(String effect) {
        for (Condition condition : conditions) {
            if (condition.name.equals(effect)) {
                return true;
            }
        }
        return false;
    }

    //Move the character based on key input. This input can come from a variety of sources, not just keyboard presses.
    public int[] move(Enums.key input) {
        int[] move = new int[2];
        switch (input) {
            case LEFT: move = new int[] {-1, 0}; System.out.println("[DBG]: Moved left."); break;
            case RIGHT: move = new int[] {1, 0}; System.out.println("[DBG]: Moved right."); break;
            case UP: move = new int[] {0, -1}; System.out.println("[DBG]: Moved up."); break;
            case DOWN: move = new int[] {0, 1}; System.out.println("[DBG]: Moved down."); break;
        }
        if (map.checkEntityPositions(x + move[0], y + move[1]) == null) {
            boolean collided = checkCollision(move[0], move[1]);
            if (!collided) {
                x += move[0];
                y += move[1];
                return move;
            }
        }
        System.out.println("[DBG]: Collided with wall.");
        return move;
    }

    //Teleport to another area of the map.
    public boolean teleport(int x, int y) {
        if (!this.map.isWall(x, y)) {
            this.x = x;
            this.y = y;
            return true;
        }
        return false;
    }

    //Change direction.
    public void setDirection(int x, int y) {
        xDir = x;
        yDir = y;
    }

    //If the character is not flying, check whether it can move where it wants to for lack of walls and other creatures.
    public boolean checkCollision(int xStep, int yStep) {
        if (map.isWall(x + xStep, y + yStep) || map.checkEntityPositions(x + xStep, y + yStep) != null) {
            return true;
        }
        return false;
    }

    //Check distance between entities or coordinates.
    public double checkDistance(Character character) {
        return checkDistance(character.x, character.y);
    }
    public double checkDistance(int x, int y) {
        return methods.distance(this.x, this.y, x, y);
    }

    //Draw the character, using rectangular sections of a sprite sheet. If there is no sprite sheet, default to a coloured square.
    //Row 1 - Down
    //Row 2 - Right
    //Row 3 - Left
    //Row 4 - Up
    public void drawSelf(Graphics g, int x, int y, int screenTileSize, Color colour) {
        if (spriteSheet != null) {
            int offset, offsetX = 0, offsetY = 0;
            BufferedImage sprite = methods.imageDeepCopy(spriteSheet);
            if (attacking) {
                offset = (int) Math.pow((attackTick - (attackTime / 2.0)), 2);
                offsetX = xDir * offset;
                offsetY = yDir * offset;
            }
            if (invulnerable) {
                methods.tintImage(sprite, Color.WHITE);
            }
            g.drawImage(sprite, x + methods.integerDivision(offsetX, tileSize, screenTileSize), y + methods.integerDivision(offsetY, tileSize, screenTileSize), null);
        }
        else {
            g.setColor(colour);
            g.fillRect(x, y, screenTileSize, screenTileSize);
        }
    }
}