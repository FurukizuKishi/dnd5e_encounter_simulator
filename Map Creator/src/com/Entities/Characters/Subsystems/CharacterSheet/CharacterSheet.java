package com.Entities.Characters.Subsystems.CharacterSheet;

import com.GUI.System.Fonts;
import com.System.InputMethods.MouseInput;
import com.globals;
import com.methods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class CharacterSheet extends JPanel {
    protected JFrame sheet;
    protected SheetComponents components;
    private String name;
    private BufferedImage sheetBackground = methods.getImage("images/character_sheet");
    protected HashMap<String, int[]> abilityScores = new HashMap<>();
    protected HashMap<String, Integer> secondaryScores = new HashMap<>();
    protected HashMap<String, Integer> classLevels = new HashMap<>();
    protected HashMap<String, Boolean> savingThrows = new HashMap<>();
    private int level = 0;
    private int experience = 0;
    private int maxHealth = 0;
    private int health = 0;
    private boolean dying = false;
    private boolean dead = false;
    private int[] deathSaves = {0, 0};

    public CharacterSheet(String name, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.name = name;
        calculateStats(strength, dexterity, constitution, intelligence, wisdom, charisma);
        assignSpeed();
    }
    public CharacterSheet(String name) {
        this(name, 10, 10, 10, 10, 10, 10);
    }

    public void calculateStats() {
        calculateStats(score("str"), score("dex"), score("con"), score("int"), score("wis"), score("cha"));
    }
    public void calculateStats(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        calculateAbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma);
        assignMaxHealth(8 + mod("CON"));
        assignProficiency();
        assignInitiative();
        assignArmour();
        assignAC();
    }

    public void calculateAbilityScores(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        assignAbilityScore("STR", strength);
        assignAbilityScore("DEX", dexterity);
        assignAbilityScore("CON", constitution);
        assignAbilityScore("INT", intelligence);
        assignAbilityScore("WIS", wisdom);
        assignAbilityScore("CHA", charisma);
    }
    public void assignAbilityScore(String score, int value) {
        abilityScores.put(score, new int[] { value, (value - 10) / 2 });
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int score(String score) {
        return abilityScores.get(score.toUpperCase())[0];
    }
    public int mod(String score) {
        return abilityScores.get(score.toUpperCase())[1];
    }
    public int speed() {
        return secondaryScores.get("Speed");
    }
    public int initiative() {
        return secondaryScores.get("Initiative");
    }
    public int prof() {
        return secondaryScores.get("Proficiency");
    }
    public int level() {
        return level;
    }
    public int experience() {
        return experience;
    }
    public int experience(int level) {
        return experience - experienceNeeded(Math.max(0, level));
    }
    public int experienceNeeded(int level) {
        switch (level) {
            case 1: return 0;
            case 2: return 300;
            case 3: return 900;
            case 4: return 2700;
            case 5: return 6500;
            case 6: return 14000;
            case 7: return 23000;
            case 8: return 34000;
            case 9: return 48000;
            case 10: return 64000;
            case 11: return 85000;
            case 12: return 100000;
            case 13: return 120000;
            case 14: return 140000;
            case 15: return 165000;
            case 16: return 195000;
            case 17: return 225000;
            case 18: return 265000;
            case 19: return 305000;
            case 20: return 355000;
            default: return -1;
        }
    }
    public int health() {
        return health;
    }
    public int maxHealth() {
        return maxHealth;
    }
    public int armour() {
        return secondaryScores.get("Armour");
    }

    public void assignAC() {
        assignAC(armour() + mod("DEX"));
    }
    public void assignAC(int value) {
        secondaryScores.put("AC", value);
    }
    public void assignArmour() {
        assignArmour(10);
    }
    public void assignArmour(int value) {
        secondaryScores.put("Armour", value);
    }
    public void assignSpeed() {
        assignSpeed(30);
    }
    public void assignSpeed(int value) {
        secondaryScores.put("Speed", value);
    }
    public void assignInitiative() {
        assignInitiative(mod("DEX"));
    }
    public void assignInitiative(int value) {
        secondaryScores.put("Initiative", value);
    }
    public void assignProficiency() {
        assignProficiency(2 + ((level + 1) / 4));
    }
    public void assignProficiency(int value) {
        secondaryScores.put("Proficiency", value);
    }
    public void assignCharacterLevel() {
        level = 0;
        for (HashMap.Entry<String, Integer> cClass : classLevels.entrySet()) {
            level += cClass.getValue();
        }
    }
    public void assignMaxHealth(int value) {
        assignMaxHealth(value, true);
    }
    public void assignMaxHealth(int value, boolean heal) {
        maxHealth = value;
        if (heal) {
            health = maxHealth;
        }
    }

    public boolean setHealth(int value) {
        if (!dead) {
            if (value <= 0) {
                if (value <= -maxHealth) {
                    dying = false;
                    dead = true;
                } else {
                    dying = true;
                }
            }
            else {
                health = value;
                dying = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("PAINT");
        g.drawImage(sheetBackground, 0, 0, null);
        repaint();
    }
    public void createInterface(SheetComponents components) {
        for (Map.Entry<int[], Object[]> component : components.components.entrySet()) {
            components.add((JComponent) component.getValue()[0]);
        }
    }
    public void display() {
        sheet = new JFrame();
        sheet.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                sheet = null;
                components = null;
            }
        });
        if (sheetBackground != null) {
            setSize(new Dimension(sheetBackground.getWidth(), sheetBackground.getHeight()));
        }
        setLocation(0, 0);
        setVisible(true);

        components = new SheetComponents(this);
        components.createComponents();
        components.setLayout(null);
        components.setSize(getSize());
        //components.setOpaque(false);
        createInterface(components);
        components.setLocation(0, 0);
        components.setVisible(true);

        JLayeredPane layers = new JLayeredPane();
        layers.setPreferredSize(getSize());
        layers.add(components, new Integer(0));
        layers.add(this, new Integer(1));
        layers.setVisible(true);

        JScrollPane scrollbar = new JScrollPane(layers);
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollbar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollbar.setVisible(true);
        sheet.add(scrollbar);

        components.addMouseListener(new MouseInput(components));
        components.addMouseMotionListener(new MouseInput(components));
        sheet.setTitle("Character sheet for " + name);
        sheet.setSize(new Dimension(getSize().width, 480));
        sheet.setVisible(true);
    }
}
