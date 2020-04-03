package com.Entities.Characters;

import com.Entities.Conditions.Condition;
import com.Entities.Entity;
import com.Entities.Pathfinder;
import com.Map.Map;
import com.System.Enums;
import com.System.Text.FloatingText;
import com.methods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class CharacterModel extends Entity implements Comparable<CharacterModel> {
    public String name;                                                     //The character's name.
    public int tileSize = 32;                                               //The size of tiles in the game.
    public boolean attacking = false;                                       //Whether the character is attacking.
    public int attackTick = 0;                                              //The timer for the character's attack.
    public int attackTime = 0;
    public boolean invulnerable = false;                                    //Whether the character is flashing white.
    public BufferedImage invulnerableSprite;
    public CharacterSheet charSheet = new CharacterSheet();                 //The character's character sheet.
    public Pathfinder pathfinder = new Pathfinder(this);            //The character's pathfinder.
    public int x;                                                           //The character's coordinates.
    public int y;
    public Rectangle boundingBox;                                           //The character's bounding box.
    public int xDir = 0;                                                    //The character's direction.
    public int yDir = 0;
    public int space;                                                       //How much space the character takes up.
    public Map map;                                                         //What map the character is currently in.
    public ArrayList<Condition> conditions = new ArrayList<>();             //All of the conditions the character has applied to them.

    public void checkHealth() {

    }

    public void damageHealth(int damage) {
        //invulnerable = true;
        charSheet.setHealth(charSheet.health() - damage);
        map.frame.textStores.get("damage").addDamageText(damage, x, y);
        checkHealth();
    }

    public void damageHealth(int damage, Color damageTextColour) {
        damageHealth(damage);
        ArrayList<FloatingText> textStore = map.frame.textStores.get("damage").floatingText;
        textStore.get(textStore.size() - 1).setColours(damageTextColour);
    }

    public boolean hasCondition(String effect) {
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

    //Change the player's current map. Alone this won't work. For a full removal, the GUI's unlinkRoom() function will need to be called.
    public void setMap(Map map) {
        this.map = map;
        pathfinder.setMap(map);
    }

    //If the character is not flying, check whether it can move where it wants to for lack of walls and other creatures.
    public boolean checkCollision(int xStep, int yStep) {
        if (map.isWall(x + xStep, y + yStep) || map.checkEntityPositions(x + xStep, y + yStep) != null) {
            return true;
        }
        return false;
    }

    //Check distance between entities or coordinates.
    public double checkDistance(CharacterModel character) {
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
        if (sprite != null) {
            int offset, offsetX = 0, offsetY = 0;
            if (this.invulnerableSprite == null) {
                this.invulnerableSprite = methods.imageDeepCopy(this.sprite);
                methods.tintImage(this.invulnerableSprite, Color.WHITE);
            }
            BufferedImage sprite = this.sprite;
            if (invulnerable) {
                sprite = this.invulnerableSprite;
            }
            if (attacking) {
                offset = (int) Math.pow((attackTick - (attackTime / 2.0)), 2);
                offsetX = xDir * offset;
                offsetY = yDir * offset;
            }
            g.drawImage(sprite,
                    x + methods.integerDivision(offsetX, tileSize, screenTileSize), y + methods.integerDivision(offsetY, tileSize, screenTileSize),
                    screenTileSize, screenTileSize,
                    null);
        }
        else {
            g.setColor(colour);
            g.fillRect(x, y, screenTileSize, screenTileSize);
        }
    }

    public int compareTo(CharacterModel o) {
        return charSheet.initiative() - o.charSheet.initiative();
    }
}