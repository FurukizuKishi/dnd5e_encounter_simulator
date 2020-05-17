package com.Game.Entities.Characters.Abilities;

public class ClassAbility extends Ability implements Comparable<ClassAbility> {
    public int level;
    public int priority;
    public boolean optional;

    public int compareTo(ClassAbility o) {
        if (level == o.level) {
            if (priority == o.priority) {
                String[] names = { name.toLowerCase(), o.name.toLowerCase() };
                if (names[0].equals(names[1])) {
                    return 0;
                }
                for (int i = 0; i < Math.min(name.length(), o.name.length()); i += 1) {
                    if (names[0].charAt(i) != names[1].charAt(i)) {
                        return names[0].charAt(i) - names[1].charAt(i);
                    }
                }
                return o.name.length() - name.length();
            }
            return o.priority - priority;
        }
        return o.level - level;
    }
}
