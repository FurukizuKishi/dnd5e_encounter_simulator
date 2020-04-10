package com.Entities.Characters.Subsystems.CharacterSheet;

import com.System.InputMethods.MouseInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SheetComponents {
    private CharacterSheet sheet;
    public HashMap<int[], JComponent> components = new HashMap<>();
    public SheetComponents(CharacterSheet sheet) {
        this.sheet = sheet;
    }

    public void createComponents() {
        createAbilityScoreField("str", 52, 254, 134, 438);
    }
    public void createAbilityScoreField(String stat, int x1, int y1, int x2, int y2) {
        createTextField(stat, sheet.abilityScores, x1, y1, x2, y2);
    }
    public void createSecondaryStatField(String stat, int x1, int y1, int x2, int y2) {
        createTextField(stat, sheet.secondaryScores, x1, y1, x2, y2);
    }
    public void createTextField(String stat, HashMap map, int x1, int y1, int x2, int y2) {
        JTextField field = new JFormattedTextField(map.get(stat));
        components.put(new int[] {x1, y1, x2, y2}, field);
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
        field.setLocation(x1, y1);
        field.setPreferredSize(new Dimension(x2 - x1, y2 - y1));
    }
    public void hoverComponents(JFrame sheet) {
        MouseInput input = (MouseInput) sheet.getMouseListeners()[0];
        Point mouse = input.getMouseLocation();
        for (Map.Entry<int[], JComponent> component : components.entrySet()) {
            int[] bounds = component.getKey();
            int x = bounds[0];
            int y = bounds[1];
            int w = bounds[2] - x;
            int h = bounds[3] - y;
            if (new Rectangle(x, y, w, h).contains(mouse.x, mouse.y)) {
                component.getValue().setVisible(true);
            } else {
                component.getValue().setVisible(false);
            }
        }
    }
}
