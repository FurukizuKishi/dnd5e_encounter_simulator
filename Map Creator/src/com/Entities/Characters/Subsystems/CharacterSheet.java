package com.Entities.Characters.Subsystems;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CharacterSheet extends JFrame {
    private HashMap<String, int[]> abilityScores = new HashMap<>();
    private HashMap<String, Integer> secondaryScores = new HashMap<>();
    private HashMap<String, Integer> classLevels = new HashMap<>();
    private HashMap<String, Boolean> savingThrows = new HashMap<>();
    private HashMap<Integer, Integer> spellSlots = new HashMap<>();
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
    public int spellSlot(int level) {
        return spellSlots.getOrDefault(level, -1);
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
    public void assignSpellSlot(int level, int number) {
        spellSlots.put(level, number);
    }
    public void assignSpellSlots(int ... slots) {
        for (int i = 0; i < slots.length; i += 1) {
            assignSpellSlot(i + 1, slots[i]);
        }
    }
    public void fullCasterSlots(int level) {
        switch (level) {
            case 1: assignSpellSlots(2);
            case 2: assignSpellSlots(3);
            case 3: assignSpellSlots(4, 2);
            case 4: assignSpellSlots(4, 3);
            case 5: assignSpellSlots(4, 3, 2);
            case 6: assignSpellSlots(4, 3, 3);
            case 7: assignSpellSlots(4, 3, 3, 1);
            case 8: assignSpellSlots(4, 3, 3, 2);
            case 9: assignSpellSlots(4, 3, 3, 3, 1);
            case 10: assignSpellSlots(4, 3, 3, 3, 2);
            case 11: assignSpellSlots(4, 3, 3, 3, 2, 1);
            case 12: assignSpellSlots(4, 3, 3, 3, 2, 1);
            case 13: assignSpellSlots(4, 3, 3, 3, 2, 1, 1);
            case 14: assignSpellSlots(4, 3, 3, 3, 2, 1, 1);
            case 15: assignSpellSlots(4, 3, 3, 3, 2, 1, 1, 1);
            case 16: assignSpellSlots(4, 3, 3, 3, 2, 1, 1, 1);
            case 17: assignSpellSlots(4, 3, 3, 3, 2, 1, 1, 1, 1);
            case 18: assignSpellSlots(4, 3, 3, 3, 3, 1, 1, 1, 1);
            case 19: assignSpellSlots(4, 3, 3, 3, 3, 2, 1, 1, 1);
            case 20: assignSpellSlots(4, 3, 3, 3, 3, 2, 2, 1, 1);
        }
    }
    public void halfCasterSlots(int level) {
        switch (level) {
            case 1: assignSpellSlots();
            case 2: assignSpellSlots(2);
            case 3: assignSpellSlots(3);
            case 4: assignSpellSlots(3);
            case 5: assignSpellSlots(4, 2);
            case 6: assignSpellSlots(4, 2);
            case 7: assignSpellSlots(4, 3);
            case 8: assignSpellSlots(4, 3);
            case 9: assignSpellSlots(4, 3, 2);
            case 10: assignSpellSlots(4, 3, 2);
            case 11: assignSpellSlots(4, 3, 3);
            case 12: assignSpellSlots(4, 3, 3);
            case 13: assignSpellSlots(4, 3, 3, 1);
            case 14: assignSpellSlots(4, 3, 3, 1);
            case 15: assignSpellSlots(4, 3, 3, 2);
            case 16: assignSpellSlots(4, 3, 3, 2);
            case 17: assignSpellSlots(4, 3, 3, 3, 1);
            case 18: assignSpellSlots(4, 3, 3, 3, 1);
            case 19: assignSpellSlots(4, 3, 3, 3, 2);
            case 20: assignSpellSlots(4, 3, 3, 3, 2);
        }
    }
    public void thirdCasterSlots(int level) {
        switch (level) {
            case 1: assignSpellSlots();
            case 2: assignSpellSlots();
            case 3: assignSpellSlots(2);
            case 4: assignSpellSlots(3);
            case 5: assignSpellSlots(3);
            case 6: assignSpellSlots(3);
            case 7: assignSpellSlots(4, 2);
            case 8: assignSpellSlots(4, 2);
            case 9: assignSpellSlots(4, 2);
            case 10: assignSpellSlots(4, 3);
            case 11: assignSpellSlots(4, 3);
            case 12: assignSpellSlots(4, 3);
            case 13: assignSpellSlots(4, 3, 2);
            case 14: assignSpellSlots(4, 3, 2);
            case 15: assignSpellSlots(4, 3, 2);
            case 16: assignSpellSlots(4, 3, 3);
            case 17: assignSpellSlots(4, 3, 3);
            case 18: assignSpellSlots(4, 3, 3);
            case 19: assignSpellSlots(4, 3, 3, 1);
            case 20: assignSpellSlots(4, 3, 3, 1);
        }
    }
    public void warlockSlots(int level) {
        spellSlots.clear();
        switch (level) {
            case 1: assignSpellSlot(1, 1);
            case 2: assignSpellSlot(1, 2);
            case 3: assignSpellSlot(2, 2);
            case 4: assignSpellSlot(2, 2);
            case 5: assignSpellSlot(3, 2);
            case 6: assignSpellSlot(3, 2);
            case 7: assignSpellSlot(4, 2);
            case 8: assignSpellSlot(4, 2);
            case 9: assignSpellSlot(5, 2);
            case 10: assignSpellSlot(5, 2);
            case 11: assignSpellSlot(5, 2);
            case 12: assignSpellSlot(5, 2);
            case 13: assignSpellSlot(5, 3);
            case 14: assignSpellSlot(5, 3);
            case 15: assignSpellSlot(5, 3);
            case 16: assignSpellSlot(5, 3);
            case 17: assignSpellSlot(5, 4);
            case 18: assignSpellSlot(5, 4);
            case 19: assignSpellSlot(5, 4);
            case 20: assignSpellSlot(5, 4);
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
