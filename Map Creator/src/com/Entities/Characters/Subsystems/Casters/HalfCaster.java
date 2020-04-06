package com.Entities.Characters.Subsystems.Casters;

public class HalfCaster extends Caster {
    public void calculateSpellSlots(int level) {
        super.calculateSpellSlots(level);
        switch (level) {
            case 1: assignSpellSlots();
            case 2: assignSpellSlots(2);
            case 3: assignSpellSlots(3);
            case 4: assignSpellSlots(3);
            case 5: assignSpellSlots(4, 2);
            case 6: assignSpellSlots(4, 2);
            case 7: assignSpellSlots(4, 3);
            case 8: assignSpellSlots(4, 3);
            case 9: assignSpellSlots(4, 3, 2);
            case 10: assignSpellSlots(4, 3, 2);
            case 11: assignSpellSlots(4, 3, 3);
            case 12: assignSpellSlots(4, 3, 3);
            case 13: assignSpellSlots(4, 3, 3, 1);
            case 14: assignSpellSlots(4, 3, 3, 1);
            case 15: assignSpellSlots(4, 3, 3, 2);
            case 16: assignSpellSlots(4, 3, 3, 2);
            case 17: assignSpellSlots(4, 3, 3, 3, 1);
            case 18: assignSpellSlots(4, 3, 3, 3, 1);
            case 19: assignSpellSlots(4, 3, 3, 3, 2);
            case 20: assignSpellSlots(4, 3, 3, 3, 2);
        }
    }
}
