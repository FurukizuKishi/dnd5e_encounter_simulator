package com.Entities.Characters.Subsystems.Casters;

public class WarlockCaster extends Caster {
    public void calculateSpellSlots(int level) {
        super.calculateSpellSlots(level);
        switch (level) {
            case 1: assignSpellSlot(1, 1);
            case 2: assignSpellSlot(1, 2);
            case 3: assignSpellSlot(2, 2);
            case 4: assignSpellSlot(2, 2);
            case 5: assignSpellSlot(3, 2);
            case 6: assignSpellSlot(3, 2);
            case 7: assignSpellSlot(4, 2);
            case 8: assignSpellSlot(4, 2);
            case 9: assignSpellSlot(5, 2);
            case 10: assignSpellSlot(5, 2);
            case 11: assignSpellSlot(5, 2);
            case 12: assignSpellSlot(5, 2);
            case 13: assignSpellSlot(5, 3);
            case 14: assignSpellSlot(5, 3);
            case 15: assignSpellSlot(5, 3);
            case 16: assignSpellSlot(5, 3);
            case 17: assignSpellSlot(5, 4);
            case 18: assignSpellSlot(5, 4);
            case 19: assignSpellSlot(5, 4);
            case 20: assignSpellSlot(5, 4);
        }
    }
}
