package com.Entities.Characters.Classes;

import com.Entities.Characters.Abilities.ClassAbility;

import java.util.ArrayList;
import java.util.Collections;

public class CharacterClass {
    protected int level = 1;
    protected String name;
    protected String archetype;
    protected ArrayList<ClassAbility> classFeatures = new ArrayList<>();
    protected ArrayList<ClassAbility> abilities = new ArrayList<>();

    public CharacterClass() {

    }
    public CharacterClass(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }
    public String getName() {
        return name;
    }
    public ArrayList<ClassAbility> getFeatures(int level) {
        ArrayList<ClassAbility> features = new ArrayList<>();
        Collections.sort(classFeatures);
        for (ClassAbility feature : classFeatures) {
            if (feature.level == level) {
                features.add(feature);
            }
        }
        return features;
    }
    public void addFeatures(int level) {
        abilities.addAll(getFeatures(level));
    }
    public void gainLevel() {
        level += 1;
        addFeatures(level);
    }
}
