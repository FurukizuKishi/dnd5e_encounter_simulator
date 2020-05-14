package com.Game.GUI;

import com.Game.Entities.Characters.CharacterModel;
import com.Game.System.Enums;
import com.Game.GUI.System.Fonts;
import com.Game.methods;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HUD {
    int sw;                                                         //The screen dimensions.
    int sh;
    Camera camera;                                                  //The camera the HUD is linked to.
    CharacterModel selected;                                        //The character the HUD is linked to.
    CharacterModel hover;                                           //The character the mouse is hovering over.
    BufferedImage healthContainer;                                  //The sprite for the healthbar's border.
    int barXOffset;                                                 //The distance between the top-left corners of the healthbar and screen.
    int barYOffset;
    Enums.alignmentHorizontal justification;                        //Text justification.

    //HUD constructor.
    public HUD(int screenW, int screenH, CharacterModel selected) {
        this.sw = screenW;
        this.sh = screenH;
        this.selected = selected;
        try {
            healthContainer = ImageIO.read(new File("sprites/HUD/Healthbar.png"));
            barXOffset = 3;
            barYOffset = 3;
        }
        catch (IOException e) {
            healthContainer = null;
        }
    }

    public CharacterModel getSelected() {
        return selected;
    }

    public boolean select(CharacterModel character) {
        if (character != selected) {
            selected = character;
            camera.selected = character;
            for (CharacterModel c : camera.map.characterList) {
                c.pathfinder.deactivate();
            }
            if (character != null) {
                character.pathfinder.activate();
            }
            return true;
        }
        return false;
    }

    //Draw the HUD, including the healthbar and minimap.
    public void paintHUD(Graphics g, Enums.alignmentHorizontal alignment, Color healthFillColour, Color moveFillColour, Color healthBackColour, Color moveBackColour, Color borderColour) {
        if (selected != null) {
            int barX, barY, barWidth, barHeight;
            if (healthContainer != null) {
                barX = 8;
                barY = 8;
                barWidth = healthContainer.getWidth() - (barXOffset * 2);
                barHeight = healthContainer.getHeight() - (barYOffset * 2);
                methods.paintValueBarSquare(g, selected.charSheet.health(), selected.charSheet.maxHealth(), healthFillColour, healthBackColour, barX + barXOffset, barY + barYOffset, barWidth, barHeight);
                g.drawImage(healthContainer, barX, barY, null);
            } else {
                barX = sw / 96;
                barY = sw / 96;
                barWidth = sw / 6;
                barHeight = sw / 24;
                int border = 2;
                methods.paintValueBarSquare(g, selected.charSheet.health(), selected.charSheet.maxHealth(), healthFillColour, healthBackColour, borderColour, barX, barY, barWidth, barHeight, border);
            }
            alignText(alignment);
            int bx = 0;
            switch (alignment) {
                case LEFT: bx = 8; break;
                case MIDDLE: bx = barWidth / 2; break;
                case RIGHT: bx = barWidth - 8; break;
            }
            drawString(g, Integer.toString(selected.charSheet.health()), barX + bx, barY + barHeight + 1, Fonts.font.DAMAGE, Color.WHITE);
            if (selected.actor.node > -1) {
                barX = sw / 96;
                barY = sw / 96 + (sw / 24);
                barWidth = sw / 6;
                barHeight = sw / 48;
                int border = 2;
                methods.paintValueBarSquare(g, selected.actor.node + 1, selected.pathfinder.getMove(), moveFillColour, moveBackColour, borderColour, barX, barY, barWidth, barHeight, border);
            }
            if (selected.map != null) {
                //character.map.drawMinimap(g, 16, 48, 160, 160);
            }
        }
    }

    //Draw the healthbar above the enemies' heads.
    public void paintHealthbar(Graphics g, Enums.alignmentHorizontal alignment, int x, int y, CharacterModel character, Color fillColour, Color backColour, Color borderColour) {
        int barSqueeze = 2;
        int barX = x + barSqueeze;
        int barY = y - 4;
        int barWidth = camera.tileSize - (barSqueeze * 2);
        int barHeight = sw / 48;
        int border = 1;
        methods.paintValueBarSquare(g, character.charSheet.health(), character.charSheet.maxHealth(), fillColour, backColour, borderColour, barX, barY, barWidth, barHeight, border);
        alignText(alignment);
        int bx = 0;
        switch (alignment) {
            case LEFT: bx = 2; break;
            case MIDDLE: bx = barWidth / 2; break;
            case RIGHT: bx = barWidth - 2; break;
        }
        drawString(g, Integer.toString(character.charSheet.health()), barX + bx, barY + barHeight, Fonts.font.DAMAGE, Color.WHITE);
    }

    //Create the light that surrounds the character, using area subtraction to cut areas out of an overlay.
    public Object[] createLight(Graphics2D g, int time, int maxTime) {
        int maxRadius = camera.tileSize * (Math.min(camera.w, camera.h) - 1) / 2;
        int radius = (int) (maxRadius * methods.doubleDivision(maxTime - time, maxTime));
        int opacity = (int) (Math.max(0.2, methods.doubleDivision(time, maxTime)) * 255);
        int width = camera.w * camera.tileSize;
        int height = camera.h * camera.tileSize;
        Area darkness = new Area(new Rectangle(0, 0, width, height));
        Ellipse2D.Double light = getCircleArea(radius, width / 2, height / 2);
        darkness.subtract(new Area(light));
        //System.out.println(methods.tuple("light", time, maxTime, "radius", radius, maxRadius, "opacity", opacity));
        return new Object[] {darkness, new Color(0, 0, 0, opacity)};
    }

    //Fill in the darkness surrounding the light.
    public void paintLight(Graphics2D g, Area darkness, Color colour) {
        g.setColor(colour);
        g.fill(darkness);
    }

    //Get the light's area.
    public Ellipse2D.Double getCircleArea(int radius, int x, int y) {
        return new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
    }

    //Draw a styled string of text.
    public void drawString(Graphics g, String str, int x, int y, Fonts.font font, Color colour) {
        int textAlignment = methods.getNewHorizontalTextAlignment(g, str, justification, font);
        methods.drawString(g, str, x - textAlignment, y, font, colour);
    }

    //Align text to a certain justification.
    public void alignText(Enums.alignmentHorizontal justification) {
        this.justification = justification;
    }
}