package com.Entities.Characters.Subsystems.CharacterSheet;

import com.GUI.System.Fonts;
import com.System.InputMethods.MouseInput;
import com.globals;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class SheetComponents extends JPanel {
    private CharacterSheet sheet;
    public HashMap<int[], Object[]> components = new HashMap<>();
    public SheetComponents(CharacterSheet sheet) {
        this.sheet = sheet;
    }

    public void createAbilityScoreField(String stat, int x, int y, int w, int h) {
        createStatField(stat, sheet.abilityScores, x, y, w, h);
    }
    public void createSecondaryStatField(String stat, int x, int y, int w, int h) {
        createStatField(stat, sheet.secondaryScores, x, y, w, h);
    }
    public void createHealthField(int x, int y, int w, int h) {
        createStatField("hp", null, x, y, w, h);
    }
    public void createMaxHealthField(int x, int y, int w, int h) {
        createStatField("hp", null, x, y, w, h);
    }

    public JTextField createTextField(String value, int alignment, int x, int y, int w, int h) {
        JTextField field = new JFormattedTextField(value);
        components.put(new int[] {x, y, x + w, y + h}, new Object[] { field, true });
        field.setBounds(x, y, w, h);
        field.setHorizontalAlignment(alignment);
        return field;
    }
    public JTextField createTextField(int value, int alignment, int x, int y, int w, int h) {
        return createTextField(Integer.toString(value), alignment, x, y, w, h);
    }
    public void createStatField(String stat, HashMap map, int x, int y, int w, int h) {
        JTextField field = null;
        if (map == sheet.abilityScores) {
            field = createTextField(sheet.score(stat), JTextField.CENTER, x, y, w, h);
        } else if (map != null) {
            field = createTextField(map.get(stat).toString(), JTextField.CENTER, x, y, w, h);
        } else {
            if (stat.equals("hp")) {
                field = createTextField(sheet.health(), JTextField.CENTER, x, y, w, h);
            }
        }
        if (field != null) {
            field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        JTextField source = (JTextField) e.getSource();
                        System.out.println(stat);
                        try {
                            int val = Integer.parseInt(source.getText());
                            if (!source.getText().equals("")) {
                                if (map != null) {
                                    if (map == sheet.abilityScores) {
                                        sheet.assignAbilityScore(stat, val);
                                        sheet.calculateStats();

                                    } else {
                                        map.put(stat, val);
                                    }
                                } else {
                                    if (stat.equals("hp")) {
                                        sheet.setHealth(val);
                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {

                        }
                    }
                }
            });
        }
    }
    public void hoverComponents() {
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
