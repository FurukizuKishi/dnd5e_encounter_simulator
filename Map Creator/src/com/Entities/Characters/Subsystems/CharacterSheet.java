package com.Entities.Characters.Subsystems;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CharacterSheet extends JFrame {
    private HashMap<String, int[]> abilityScores = new HashMap<>();
    private HashMap<String, Integer> secondaryScores = new HashMap<>();
    private HashMap<String, Integer> classLevels = new HashMap<>();
    private HashMap<String, Boolean> savingThrows = new HashMap<>();
    private int level = 0;
    private int experience = 0;
    private int maxHealth = 0;
    private int health = 0;
    private boolean dying = false;
    private boolean dead = false;
    private int[] deathSaves = {0, 0};

    public CharacterSheet(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        calculateStats(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }
    public CharacterSheet() {
        calculateAbilityScores(10, 10, 10, 10, 10, 10);
    }

    public void calculateStats(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        calculateAbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma);
        assignMaxHealth(8 + mod("CON"));
        assignProficiency();
        assignInitiative();
        assignArmour();
        assignAC();
        assignSpeed();
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

    public void display(Graphics g) {

    }
}