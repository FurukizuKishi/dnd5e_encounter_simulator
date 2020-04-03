package com.Entities;

import com.Entities.Characters.CharacterModel;
import com.Map.Map;
import com.System.Enums;
import com.methods;

import java.util.ArrayList;

public class Pathfinder {
    private CharacterModel owner;
    private int move;
    private int range;
    public Enums.pathTile[][] pathGrid;
    private Map map;
    private boolean active = false;
    public Pathfinder(CharacterModel owner, int move, int range, Map map) {
        this.owner = owner;
        this.move = move;
        this.range = range;
        this.map = map;
    }
    public Pathfinder(CharacterModel owner, int move, Map map) {
        this(owner, move, 1, map);
    }
    public Pathfinder(CharacterModel owner, Map map) {
        this(owner, 3, 1, map);
    }
    public Pathfinder(CharacterModel owner) {
        this(owner, 3, 1, null);
    }

    //Set the pathfinder's map.
    public void setMap(Map map) {
        this.map = map;
    }

    //Set whether the pathfinder is active to show its character's movement options.
    public void activate(boolean active) {
        this.active = active;
        if (active) {
            createMovementRange();
            display();
        }
    }
    //Check whether the pathfinder is active.
    public boolean isActive() {
        return active;
    }

    //Pathfind towards the player, one step at a time. This function checks each of the enemy's adjacent walls to see where it can move that reduces
    //its distance to the player.
    public int[] pathfind(ArrayList<int[]> path, int x1, int y1, int x2, int y2) {
        ArrayList<int[]> dir = new ArrayList<>();
        for (int i = 0; i < 4; i += 1) {
            int nx = x1;
            int ny = y1;
            switch (i) {
                case 0:
                    ny -= 1;
                    break;
                case 1:
                    nx += 1;
                    break;
                case 2:
                    ny += 1;
                    break;
                case 3:
                    nx -= 1;
                    break;
            }
            if (!(map.isWall(nx, ny) || methods.steppingBackwards(path, nx, ny))) {
                int steps = 1;
                switch (map.map[ny][nx]) {
                    case DIFFICULT_TERRAIN: steps = 2; break;
                    case WATER: steps = 2; break;
                }
                dir.add(new int[]{nx, ny, steps});
            }
        }
        int[] bestNode = null;
        double bestDist = Double.POSITIVE_INFINITY;
        for (int[] node : dir) {
            double dist = methods.distance(node[0], node[1], x2, y2);
            if (dist < bestDist) {
                bestDist = dist;
                bestNode = node;
            }
        }
        return bestNode;
    }

    public int findPathDistance(int x, int y) {
        if (map.isWall(x, y)) {
            return -1;
        }
        else {
            int steps = 0;
            ArrayList<int[]> path = new ArrayList<>();
            path.add(pathfind(path, owner.x, owner.y, x, y));
            if (path.get(0) == null) {
                return -1;
            }
            else {
                try {
                    int[] node = path.get(path.size() - 1);
                    while (node != null && steps < move) {
                        steps += node[2];
                        if (steps < move) {
                            path.add(pathfind(path, node[0], node[1], x, y));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return -1;
                }
                return path.size();
            }
        }
    }

    public void createMovementRange() {
        move = owner.charSheet.speed() / 5;
        pathGrid = new Enums.pathTile[map.h][map.w];
        for (int y = 0; y < map.h; y += 1) {
            for (int x = 0; x < map.w; x += 1) {
                pathGrid[y][x] = Enums.pathTile.NULL;
                if (findPathDistance(x, y) > -1) {
                    if (findPathDistance(x, y) <= move) {
                        pathGrid[y][x] = Enums.pathTile.FREE;
                    } else if (findPathDistance(x, y) <= move + range) {
                        pathGrid[y][x] = Enums.pathTile.ATTACK;
                    }
                }
            }
        }
    }

    //Print the map's layout to the terminal.
    public void display() {
        System.out.println("Map : " + this);
        for (int y = 0; y < pathGrid.length; y += 1) {
            for (int x = 0; x < pathGrid[y].length; x += 1) {
                char icon = ' ';
                switch (pathGrid[y][x]) {
                    case NULL:
                        icon = ' ';
                        break;      // no-move
                    case FREE:
                        icon = '-';
                        break;      // move
                    case ATTACK:
                        icon = '+';
                        break;      // range
                }
                if (x == owner.x && y == owner.y) {
                    icon = owner.name.toUpperCase().charAt(0);
                }
                System.out.print(icon + " ");
            }
            System.out.println();
        }
    }
}