package com.System;

import com.methods;

import java.util.Arrays;
import java.util.List;

import static com.System.Enums.damageType.*;

public class DamageTypes {
    public static List<String> names = Arrays.asList("Bludgeoning", "Piercing", "Slashing", "Healing", "Acid", "Cold", "Fire", "Force", "Lightning", "Necrotic", "Poison", "Psychic", "Thunder", "Radiant", "Arcane", "Righteous", "Vile");
    public static List<Enums.damageType> codes = Arrays.asList(BLUDGEONING, PIERCING, SLASHING, HEALING, ACID, COLD, FIRE, FORCE, LIGHTNING, NECROTIC, POISON, PSYCHIC, THUNDER, RADIANT, ARCANE, RIGHTEOUS, VILE);

    public static String getName(Enums.damageType type) {
        return names.get(codes.indexOf(type));
    }
    public static Enums.damageType getCode(String type) {
        return codes.get(names.indexOf(type));
    }
}
