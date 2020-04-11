package com.Entities.Characters.Subsystems.CharacterSheet;

import com.GUI.System.Fonts;
import com.System.InputMethods.MouseInput;
import com.globals;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SheetComponents extends JPanel {
    private CharacterSheet sheet;
    public HashMap<int[], Object[]> components = new HashMap<>();
    public SheetComponents(CharacterSheet sheet) {
        this.sheet = sheet;
    }

    public void createComponents() {
        createAbilityScoreField("str", 52, 254, 134, 347);
    }
    public void createAbilityScoreField(String stat, int x1, int y1, int x2, int y2) {
        createTextField(stat, sheet.abilityScores, x1, y1, x2, y2);
    }
    public void createSecondaryStatField(String stat, int x1, int y1, int x2, int y2) {
        createTextField(stat, sheet.secondaryScores, x1, y1, x2, y2);
    }
    public void createTextField(String stat, HashMap map, int x1, int y1, int x2, int y2) {
        JTextField field = new JFormattedTextField(map.get(stat));
        components.put(new int[] {x1, y1, x2, y2}, new Object[] { field, true });
        field.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (source.getText().equals("") || !(Pattern.matches("^[0-9]+$", source.getText()))) {
                    if (map == sheet.abilityScores) {
                        sheet.assignAbilityScore(stat, Integer.parseInt(source.getText()));
                        sheet.calculateStats();
                    } else {
                        map.put(stat, Integer.parseInt(source.getText()));
                    }
                }
            }
        });
        field.setBounds(x1, y1, x2 - x1, y2 - y1);
    }
    public void hoverComponents() {
        System.out.println("HOVER");
        MouseInput input = (MouseInput) getMouseMotionListeners()[0];
        Point mouse = input.getMouseLocation();
        for (Map.Entry<int[], Object[]> pair : components.entrySet()) {
            int[] bounds = pair.getKey();
            int x = bounds[0];
            int y = bounds[1];
            int w = bounds[2] - x;
            int h = bounds[3] - y;
            JComponent jComp = (JComponent) pair.getValue()[0];
            boolean visible = (boolean) pair.getValue()[1];
            if (new Rectangle(x, y, w, h).contains(mouse.x, mouse.y)) {
                if (!visible) {
                    add(jComp);
                    components.put(bounds, new Object[]{jComp, true});
                    System.out.println(true);
                }
            } else if (visible) {
                remove(jComp);
                components.put(bounds, new Object[] {jComp, false});
                System.out.println(false);
            }
        }
    }

    public void paintBar(Graphics g, int value, int maxValue, Color fillColour, Color backColour, int x1, int y1, int width, int height) {
        methods.paintValueBarSquare(g, value, maxValue, fillColour, backColour, x1, y1, width, height);
    }
    public void paintHealthbar(Graphics g, int x1, int y1, int x2, int y2) {
        paintBar(g, sheet.health(), sheet.maxHealth(), globals.healthFillColour, globals.healthBackColour, x1, y1, x2 - x1, y2 - y1);
    }
    public void paintExperienceBar(Graphics g, int x1, int y1, int x2, int y2) {
        paintBar(g, sheet.experience(sheet.level()), sheet.experience(sheet.level() + 1), globals.expFillColour, globals.expBackColour, x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintHealthbar(g, 169, 340, 459, 448);
        paintExperienceBar(g, 61, 182, 235, 215);
        String[] scores = { "str", "dex", "con", "int", "wis", "cha" };
        for (int i = 0; i < scores.length; i += 1) {
            methods.drawString(g, Integer.toString(sheet.score(scores[i])), 86, 316 + (i * 127), Fonts.font.TEXT, Color.BLACK, 4);
            methods.drawString(g, Integer.toString(sheet.mod(scores[i])), 96, 368 + (i * 127), Fonts.font.DAMAGE, Color.BLACK, 4);
        }
        if (sheet.sheet != null && sheet.components == this) {
            hoverComponents();
        }
        repaint();
    }
}
