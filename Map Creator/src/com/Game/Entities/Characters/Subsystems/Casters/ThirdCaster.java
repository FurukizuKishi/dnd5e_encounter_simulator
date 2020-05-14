package com.Game.Entities.Characters.Subsystems.Casters;

public class ThirdCaster extends Caster {
    public void calculateSpellSlots(int level) {
        super.calculateSpellSlots(level);
        switch (level) {
            case 1: assignSpellSlots(); break;
            case 2: assignSpellSlots(); break;
            case 3: assignSpellSlots(2); break;
            case 4: assignSpellSlots(3); break;
            case 5: assignSpellSlots(3); break;
            case 6: assignSpellSlots(3); break;
            case 7: assignSpellSlots(4, 2); break;
            case 8: assignSpellSlots(4, 2); break;
            case 9: assignSpellSlots(4, 2); break;
            case 10: assignSpellSlots(4, 3); break;
            case 11: assignSpellSlots(4, 3); break;
            case 12: assignSpellSlots(4, 3); break;
            case 13: assignSpellSlots(4, 3, 2); break;
            case 14: assignSpellSlots(4, 3, 2); break;
            case 15: assignSpellSlots(4, 3, 2); break;
            case 16: assignSpellSlots(4, 3, 3); break;
            case 17: assignSpellSlots(4, 3, 3); break;
            case 18: assignSpellSlots(4, 3, 3); break;
            case 19: assignSpellSlots(4, 3, 3, 1); break;
            case 20: assignSpellSlots(4, 3, 3, 1); break;
        }
    }
}
