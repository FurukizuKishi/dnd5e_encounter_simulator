package com.Entities.Characters;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CharacterSheet extends JFrame {
    private HashMap<String, int[]> abilityScores = new HashMap<>();
    private HashMap<String, Integer> secondaryScores = new HashMap<>();
    private HashMap<String, Integer> classLevels = new HashMap<>();
    private HashMap<String, Boolean> savingThrows = new HashMap<>();
    private HashMap<String, Integer> spellSlots = new HashMap<>();
    private int level = 0;
    private int experience = 0;
    private int maxHealth = 0;
    private int health = 0;

    public CharacterSheet(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        calculateAbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }
    public CharacterSheet() {
        calculateAbilityScores(10, 10, 10, 10, 10, 10);
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

    public int mod(String score) {
        return abilityScores.get(score.toUpperCase())[1];
    }
    public int speed() {
        return secondaryScores.get("Speed");
    }
    public int init() {
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

    public void assignAC() {
        secondaryScores.put("AC", 10 + mod("DEX"));
    }
    public void assignAC(int value) {
        secondaryScores.put("AC", value);
    }
    public void assignSpeed(int value) {
        secondaryScores.put("Speed", value);
    }
    public void assignInitiative() {
        secondaryScores.put("Initiative", mod("DEX"));
    }
    public void assignInitiative(int value) {
        secondaryScores.put("Proficiency", value);
    }
    public void assignProficiency() {
        secondaryScores.put("Proficiency", 2 + ((level + 1) / 4));
    }
    public void assignProficiency(int value) {
        secondaryScores.put("Initiative", value);
    }
    public void assignCharacterLevel() {
        level = 0;
        for (HashMap.Entry<String, Integer> cClass : classLevels.entrySet()) {
            level += cClass.getValue();
        }
    }
    public void assignSpellSlots(int ... levels) {

    }

    public void display(Graphics g) {

    }
}
