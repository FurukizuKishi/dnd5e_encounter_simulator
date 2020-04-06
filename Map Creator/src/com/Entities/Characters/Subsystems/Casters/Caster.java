package com.Entities.Characters.Subsystems.Casters;

import com.Entities.Characters.CharacterModel;
import com.System.Enums;

import java.util.HashMap;

public class Caster {
    CharacterModel owner;
    Enums.characterClass characterClass;
    HashMap<Integer, Integer> spellSlots = new HashMap<>();
    HashMap<String, Integer> resources = new HashMap<>();

    public Caster() {

    }
    public Caster(CharacterModel owner, Enums.characterClass characterClass) {
        this.owner = owner;
        this.characterClass = characterClass;
        calculateSpellSlots(owner.charSheet.level());
    }

    public void assignSpellSlot(int level, int number) {
        spellSlots.put(level, number);
    }
    public void assignSpellSlots(int ... slots) {
        for (int i = 0; i < slots.length; i += 1) {
            assignSpellSlot(i + 1, slots[i]);
        }
    }
    public int spellSlot(int level) {
        return spellSlots.getOrDefault(level, -1);
    }

    public void calculateSpellSlots(int level) {
        spellSlots.clear();
    }
}
