package com.Game.Entities.Characters.Subsystems.CharacterSheet;

import com.Game.GUI.System.Fonts;
import com.Game.methods;

import java.awt.*;

public class SheetComponents extends HoverComponent {
    public SheetComponents(CharacterSheet sheet) {
        super(sheet);
    }

    public void createComponents() {
        String[] scores = { "str", "dex", "con", "int", "wis", "cha" };
        for (int i = 0; i < scores.length; i += 1) {
            createAbilityScoreField(scores[i], 52, 254 + (i * 127), 83, 95);
        }
        createHealthField(185, 343, 116, 105);
        //createMaxHealthField(169, 249, 102, 84);
    }

    @Override
    public void paintComponent(Graphics g) {
        paintHealthbar(g, 169, 340, 459, 448);
        paintExperienceBar(g, 61, 182, 235, 215);
        String[] scores = { "str", "dex", "con", "int", "wis", "cha" };
        for (int i = 0; i < scores.length; i += 1) {
            methods.drawString(g, Integer.toString(sheet.score(scores[i])), 92, 326 + (i * 127), Fonts.font.TEXT, Color.BLACK, 4);
        }
        methods.drawString(g, Integer.toString(sheet.level()), 1028, 142, Fonts.font.DAMAGE, Color.BLACK, 1);
        methods.drawString(g, Integer.toString(sheet.experience(sheet.level())), 64, 218, Fonts.font.DAMAGE, Color.WHITE, 3);
        methods.drawString(g, Integer.toString(sheet.experienceNeeded(sheet.level() + 1)), 218, 214, Fonts.font.DAMAGE, Color.WHITE, 5);

        methods.drawString(g, Integer.toString(sheet.health()), 243, 425, Fonts.font.TEXT, Color.WHITE, 4);
        methods.drawString(g, Integer.toString(sheet.maxHealth()), 220, 321, Fonts.font.TEXT, Color.BLACK, 4);

        methods.drawString(g, Integer.toString(sheet.AC()), 320, 302, Fonts.font.TEXT, Color.BLACK, 4);
        methods.drawString(g, "+" + sheet.prof(), 414, 308, Fonts.font.TEXT, Color.BLACK, 4);
        super.paintComponent(g);
        if (sheet.sheet != null && sheet.components == this) {
            hoverComponents();
        }
        repaint();
    }
}
