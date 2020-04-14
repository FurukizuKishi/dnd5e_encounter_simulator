package com.Entities.Characters;

import com.Entities.Characters.Subsystems.CharacterSheet.CharacterSheet;

public class PlayerModel extends CharacterModel {
    public PlayerModel(String name, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.name = name;
        charSheet = new CharacterSheet(name, strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public int compareTo(CharacterModel o) {
        return super.compareTo(o);
    }
}