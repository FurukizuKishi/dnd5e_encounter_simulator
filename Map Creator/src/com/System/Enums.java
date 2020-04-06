package com.System;

public class Enums {
    //Types of valid key presses.
    public enum key {
        LEFT, RIGHT, UP, DOWN, ATTACK, CANCEL
    }
    //Types of spells.
    public enum spellType {
        MELEE, PROJECTILE, ARTILLERY_AIMED, ALTER_TERRAIN, SUMMON
    }
    //Types of spell shapes.
    public enum spellShape {
        LINE, CIRCLE, SQUARE
    }
    //Types of horizontal text alignment.
    public enum alignmentHorizontal {
        LEFT, MIDDLE, RIGHT
    }
    //Types of vertical text alignment.
    public enum alignmentVertical {
        TOP, MIDDLE, BOTTOM
    }
    //What happens when an animation ends.
    public enum animationEnd {
        DESTROY, REPEAT
    }
    //The types of damage an attack can deal.
    public enum damageType {
        BLUDGEONING, PIERCING, SLASHING, ACID, COLD, FIRE, FORCE, LIGHTNING, NECROTIC, POISON, PSYCHIC, THUNDER, RADIANT, ARCANE, RIGHTEOUS, VILE
    }
    //The various classes assignable to players.
    public enum characterClass {
        ARTIFICER, BARBARIAN, BLOOD_HUNTER, CLERIC, DRUID, FIGHTER, MONK, MYSTIC, PALADIN, RANGER, ROGUE, SORCERER, WARLOCK, WIZARD
    }
    //Whether a map is randomly generated or static.
    public enum mapLayout {
        RANDOMLY_GENERATED, STATIC
    }
    //Types of tiles in relation to terrain.
    public enum tileType {
        FLOOR, WALL, DIFFICULT_TERRAIN, WATER
    }
    //Types of tiles in regards to character pathfinding.
    public enum pathTile {
        NULL, FREE, ATTACK
    }
    //Types of action.
    public enum actionType {
        MOVE, ACTION, BONUS_ACTION, REACTION, LEGENDARY_ACTION, LAIR_ACTION, MYTHIC_ACTION, INTERACTION
    }
    //The actions a character can perform.
    public enum action {
        DASH, DODGE, ONE_ATTACK, FULL_ATTACK, SPELL, USE_AN_ITEM,
    }
}