package com.Game.Entities.Characters.Conditions;

import com.Game.Entities.Characters.CharacterModel;

public abstract class Condition {
    public String name;
    public int level;
    public CharacterModel target;
    public int duration;
    public int effectGap;
    public int timer;
    public int decisecond = 0;
    public int timeGap = 10;

    public void setAttributes(CharacterModel target, int level, int duration, int timeGap) {
        this.target = target;
        this.level = level;
        this.duration = duration;
        this.timeGap = timeGap;
        this.timer = 0;
    }

    public void applyEffect() {
        if (decisecond == timeGap) {
            decisecond = 0;
            String applyWord;
            if (timer < duration) {
                if (timer == 0) {
                    startEffect();
                    applyWord = "started";
                } else {
                    continueEffect();
                    applyWord = "continued";
                }
                timer += 1;
            } else {
                endEffect();
                applyWord = "ended";
                target.conditions.remove(this);
                target = null;
            }
            //System.out.println("[DBG]: " + hashCode() + " " + applyWord + " " + name + " effect. (" + timer + " / " + duration + ")");
        }
    }

    public abstract void startEffect();
    public abstract void continueEffect();
    public abstract void endEffect();
}
