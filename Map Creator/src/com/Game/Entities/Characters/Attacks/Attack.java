package com.Game.Entities.Characters.Attacks;

import com.Game.System.DamageTypes;
import com.Game.System.Die;
import com.Game.System.Enums;

import java.util.ArrayList;

public class Attack {
    private String name;
    private Die[] damage;
    private Enums.damageType damageType;
    private Enums.attackType attackType;

    public Attack(String name, Enums.attackType attackType, Enums.damageType damageType, String ... damageDice) {
        this.name = name;
        this.damageType = damageType;
        this.attackType = attackType;
        for (String die : damageDice) {

        }
    }
    public Attack(String name, Enums.attackType attackType, Enums.damageType damageType, String damageDice) {
        this(name, attackType, damageType, damageDice.replace(" ", "").split("\\+"));
    }

    public String toString(boolean includeType) {
        if (damage.length > 0) {
            String str = "" + damage[0];
            for (int i = 1; i < damage.length; i += 1) {
                str += " + " + damage[i].toString();
            }
            if (includeType) {
                return str + " " + DamageTypes.getName(damageType).toLowerCase();
            }
            return str;
        }
        return "";
    }
    public String toString() {
        return toString(false);
    }

    public int roll() {
        int roll = 0;
        for (int[] die : rollOut()) {
            for (int value : die) {
                roll += value;
            }
        }
        return roll;
    }
    public ArrayList<int[]> rollOut() {
        ArrayList<int[]> list = new ArrayList<>();
        for (Die die : damage) {
            list.add(die.rollOut());
        }
        return list;
    }
}