package com.Entities.Characters.Subsystems.Casters;

public class WarlockCaster extends Caster {
    public void calculateSpellSlots(int level) {
        super.calculateSpellSlots(level);
        switch (level) {
            case 1: assignSpellSlot(1, 1); break;
            case 2: assignSpellSlot(1, 2); break;
            case 3: assignSpellSlot(2, 2); break;
            case 4: assignSpellSlot(2, 2); break;
            case 5: assignSpellSlot(3, 2); break;
            case 6: assignSpellSlot(3, 2); break;
            case 7: assignSpellSlot(4, 2); break;
            case 8: assignSpellSlot(4, 2); break;
            case 9: assignSpellSlot(5, 2); break;
            case 10: assignSpellSlot(5, 2); break;
            case 11: assignSpellSlot(5, 2); break;
            case 12: assignSpellSlot(5, 2); break;
            case 13: assignSpellSlot(5, 3); break;
            case 14: assignSpellSlot(5, 3); break;
            case 15: assignSpellSlot(5, 3); break;
            case 16: assignSpellSlot(5, 3); break;
            case 17: assignSpellSlot(5, 4); break;
            case 18: assignSpellSlot(5, 4); break;
            case 19: assignSpellSlot(5, 4); break;
            case 20: assignSpellSlot(5, 4); break;
        }
    }
}
