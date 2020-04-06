package com.Entities.Characters.Subsystems;

import com.Entities.Characters.CharacterModel;
import com.System.Enums;

import java.util.HashMap;
import java.util.Map;

public class Actor {
    CharacterModel owner;
    private HashMap<Enums.actionType, int[]> actions = new HashMap<>();

    public Actor(CharacterModel owner) {
        this.owner = owner;
        assignActions();
    }

    public void replenishActions() {
        gainAction(Enums.actionType.MOVE);
        gainAction(Enums.actionType.ACTION);
        gainAction(Enums.actionType.REACTION);
        if (actions.get(Enums.actionType.BONUS_ACTION) != null) {
            gainAction(Enums.actionType.BONUS_ACTION);
        }
        if (actions.get(Enums.actionType.LEGENDARY_ACTION) != null) {
            gainAction(Enums.actionType.LEGENDARY_ACTION);
        }
        if (actions.get(Enums.actionType.LAIR_ACTION) != null) {
            gainAction(Enums.actionType.LAIR_ACTION);
        }
        if (actions.get(Enums.actionType.MYTHIC_ACTION) != null) {
            gainAction(Enums.actionType.MYTHIC_ACTION);
        }
    }

    public void assignAction(Enums.actionType action) {
        assignAction(action, 1);
    }
    public void assignAction(Enums.actionType action, int number) {
        actions.put(action, new int[] {number, number});
    }
    public void assignActions() {
        assignAction(Enums.actionType.MOVE, 1);
        assignAction(Enums.actionType.ACTION, 1);
        assignAction(Enums.actionType.REACTION, 1);
    }

    public boolean reduceAction(Enums.actionType action) {
        return reduceAction(action, 1);
    }
    public boolean reduceAction(Enums.actionType action, int number) {
        int[] set = actions.get(action);
        if (set[0] >= number) {
            actions.put(action,  new int[] {set[0] - number, set[1]});
            return true;
        }
        return false;
    }

    public boolean gainAction(Enums.actionType action) {
        return gainAction(action, 99);
    }
    public boolean gainAction(Enums.actionType action, int number) {
        int[] set = actions.get(action);
        if (!owner.hasCondition("Paralyzed", "Stunned")) {
            actions.put(action,  new int[] {Math.max(0, Math.min(set[0] + number, set[1])), set[1]});
            return true;
        }
        setAction(action, 0);
        return false;
    }

    public int getAction(Enums.actionType action) {
        return actions.get(action)[0];
    }
    public int getActionMaximum(Enums.actionType action) {
        return actions.get(action)[1];
    }
    public void setAction(Enums.actionType action, int number) {
        int[] set = actions.get(action);
        actions.put(action,  new int[] {Math.max(0, Math.min(number, set[1])), set[1]});
    }

    public boolean done() {
        for (Enums.actionType action : actions.keySet()) {
            if (getAction(action) > 0) {
                return false;
            }
        }
        return true;
    }

    public void makeDeathSaves() {

    }
    public void makeSavingThrows(boolean start) {

    }

    public void performAction(Enums.actionType action) {
        if (reduceAction(action)) {

        }
    }

    public void updateRound() {

    }
}
