package com.Game.Entities.Characters.Abilities;

public class ClassAbility implements Comparable<ClassAbility> {
    public int level;
    public int priority;
    public boolean optional;

    public int compareTo(ClassAbility o) {
        if (level == o.level) {
            return o.priority - priority;
        }
        return o.level - level;
    }
}
