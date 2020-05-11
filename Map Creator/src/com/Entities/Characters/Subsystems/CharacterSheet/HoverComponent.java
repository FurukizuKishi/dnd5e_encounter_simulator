package com.Entities.Characters.Subsystems.CharacterSheet;

import com.System.InputMethods.MouseInput;
import com.globals;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class HoverComponent extends JPanel {
    protected CharacterSheet sheet;
    public HashMap<int[], Object[]> components = new HashMap<>();
    public HashMap<String, JTextField> componentKeys = new HashMap<>();

    public HoverComponent() {

    }
    public HoverComponent(CharacterSheet sheet) {
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
            componentKeys.put(stat, field);
            field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        JTextField source = (JTextField) e.getSource();
                        System.out.println(stat);
                        try {
                            int val = Integer.parseInt(source.getText());
                            if (!source.getText().equals("")) {
                                updateStat(source, map, stat, val);
                                System.out.println(methods.tuple("FLAG", sheet, sheet.character));
                                sheet.getGame().host.setFlag("CHAR", sheet.character,  stat, val);
                            }
                        } catch (NumberFormatException ex) {

                        }
                    }
                }
            });
        }
    }
    public HashMap getMapForStat(String stat) {
        if (sheet.abilityScores.get(stat) != null) {
            return sheet.abilityScores;
        }
        else if (sheet.secondaryScores.get(stat) != null) {
            return sheet.abilityScores;
        }
        return null;
    }

    public void updateStat(String stat, int value) {
        updateStat(null, null, stat, value);
    }
    public void updateStat(JTextField source, HashMap map, String stat, int value) {
        if (map == null && !stat.equals("hp")) {
            map = getMapForStat(stat);
        }
        if (source == null) {
            source = componentKeys.get(stat);
        }
        try {
            if (map != null) {
                if (map == sheet.abilityScores) {
                    sheet.assignAbilityScore(stat, value);
                    sheet.calculateStats();
                } else {
                    map.put(stat, value);
                }
                source.setText(map.get(stat).toString());
            } else {
                if (stat.equals("hp")) {
                    sheet.setHealth(value);
                    source.setText(Integer.toString(sheet.health()));
                }
            }
        }
        catch (NullPointerException e) {
            System.out.println("Could not find a field for '" + stat + "'.");
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
        paintBar(g, sheet.health(), sheet.maxHealth(), sheet.healthFillColour, sheet.healthBackColour, x1, y1, x2 - x1, y2 - y1);
    }
    public void paintExperienceBar(Graphics g, int x1, int y1, int x2, int y2) {
        paintBar(g, sheet.experience(sheet.level()), sheet.experience(sheet.level() + 1), globals.expFillColour, globals.expBackColour, x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sheet.sheet != null && sheet.components == this) {
            hoverComponents();
        }
        repaint();
    }
}
