package com.Game.Entities.Characters;

import com.Game.Entities.Characters.Subsystems.CharacterSheet.CharacterSheet;
import com.Game.GUI.GUI;

public class PlayerModel extends CharacterModel {
    public PlayerModel(String name, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.name = name;
        charSheet = new CharacterSheet(this, name, strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public void setFrame(GUI frame) {
        super.setFrame(frame);
        charSheet.setGame(frame);
    }

    public int compareTo(CharacterModel o) {
        return super.compareTo(o);
    }
}