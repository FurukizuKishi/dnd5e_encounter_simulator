package com.Game.Entities.Characters.Subsystems;

import com.Game.Entities.Characters.CharacterModel;
import com.Game.GUI.GUI;
import com.Game.System.Enums;

import java.util.ArrayList;
import java.util.HashMap;

public class Actor {
    CharacterModel owner;                                                   //The character that owns this actor.
    private HashMap<Enums.actionType, int[]> actions = new HashMap<>();     //A map of all of the character's actions.
    public boolean moving = false;                                          //Whether the character is moving on the map.
    public int node = -1;                                                   //Index of the node on the path the character needs to move through.

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

    public boolean move(ArrayList<int[]> path) {
        owner.pathfinder.path = path;
        return move();
    }
    public boolean move() {
        moving = false;
        if (owner.pathfinder.path.size() > node + 1) {
            if (Math.max(owner.pathfinder.path.size(), Math.min(node, owner.pathfinder.getMove() - 1)) != node) {
                node += 1;
                moving = true;
            }
        }
        return moving;
    }
    public boolean moveToNode() {
        if (owner.pathfinder.path.size() > 0) {
            if (Math.max(0, Math.min(node, owner.pathfinder.path.size() - 1)) == node) {
                int[] node = owner.pathfinder.path.get(this.node);
                owner.pathfinder.printPath();
                if (!owner.moveToCoordinate(node[0] * owner.map.camera.tileSize, node[1] * owner.map.camera.tileSize, 2)) {
                    this.node += 1;
                }
                return true;
            }
            if (node < owner.pathfinder.getMove() - 1) {
                closePath(node, true);
            } else {
                ((GUI) owner.map.camera.getGame()).host.setMoveFlag(owner, owner.pathfinder.path);
                closePath(-1, true);
            }
        }
        return false;
    }
    public void closePath(int node, boolean clear) {
        this.node = node;
        owner.pathfinder.path = null;
        owner.pathfinder.activate(clear);
        moving = false;
    }
}
