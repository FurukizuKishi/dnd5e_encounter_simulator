package com.Entities.Characters.Subsystems;

import com.Entities.Characters.CharacterModel;
import com.Map.Map;
import com.System.Enums;
import com.methods;

import java.util.ArrayList;

public class Pathfinder {
    private CharacterModel owner;
    private int move;
    private int range;
    private int maxRange;
    public Enums.pathTile[][] pathGrid;
    public int[][] distGrid;
    public ArrayList<int[]> path = new ArrayList<>();
    private Map map;
    private boolean active = false;

    //Pathfinder constructor.
    public Pathfinder(CharacterModel owner, int move, int range, Map map) {
        this.owner = owner;
        this.map = map;
        setMovement(move);
        setRange(range);
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

    //Get the movement range.
    public int getMove() {
        return move;
    }
    //Get the attack range.
    public int getRange() {
        return range;
    }
    //Set the pathfinder's map.
    public void setMap(Map map) {
        this.map = map;
    }

    //Set the pathfinder's movement.
    public void setMovement(int value) {
        move = value;
        calculateMaxRange();
    }
    //Set the pathfinder's range.
    public void setRange(int value) {
        range = value;
        calculateMaxRange();
    }
    //Calculate the maximum range of the pathfinder algorithm.
    public void calculateMaxRange() {
        maxRange = move + range + 1;
    }

    public void activate() {
        activate(true);
    }
    public void activate(boolean clear) {
        activate(true, clear);
    }
    public void deactivate() {
        deactivate(true);
    }
    public void deactivate(boolean clear) {
        activate(false, clear);
    }
    //Set whether the pathfinder is active to show its character's movement options.
    public void activate(boolean active, boolean clear) {
        this.active = active;
        if (clear) {
            path.clear();
        }
        if (active) {
            createMovementRange();
            display();
            showDistances();
        }
    }
    //Check whether the pathfinder is active.
    public boolean isActive() {
        return active;
    }

    //Check if one cell is adjacent to another.
    public boolean adjacentCells(int x1, int y1, int x2, int y2) {
        boolean horizontal = (Math.abs(x1 - x2) == 1);
        boolean vertical = (Math.abs(y1 - y2) == 1);
        boolean far = (Math.abs(x1 - x2) > 1 || Math.abs(y1 - y2) > 1);
        if ((horizontal || vertical) && !((horizontal && vertical) || far)) {
            return true;
        }
        return false;
    }
    //Check if one cell is close to another.
    public boolean closeCells(int x1, int y1, int x2, int y2) {
        boolean horizontal = (Math.abs(x1 - x2) == 1);
        boolean vertical = (Math.abs(y1 - y2) == 1);
        boolean far = (Math.abs(x1 - x2) > 1 || Math.abs(y1 - y2) > 1);
        if ((horizontal || vertical) && !far) {
            return true;
        }
        return false;
    }

    //Print the path.
    public void printPath() {
        for (int[] node : path) {
            System.out.print(methods.tuple(node[0], node[1]) + ", ");
        }
        System.out.println();
    }

    //Pathfind towards the player, one step at a time. This function checks each of the enemy's adjacent walls to see where it can move that reduces
    //its distance to the player.
    public int[] pathfind(ArrayList<int[]> path, int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            return null;
        }
        else {
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
                        case DIFFICULT_TERRAIN:
                            steps = 2;
                            break;
                        case WATER:
                            steps = 2;
                            break;
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
    }

    //Find the walking distance from the character to a given node.
    public int findPathDistance(int x, int y) {
        if (map.isWall(x, y)) {
            return -1;
        }
        else {
            int steps = -1;
            ArrayList<int[]> path = new ArrayList<>();
            path.add(pathfind(path, owner.x, owner.y, x, y));
            if (path.get(0) == null) {
                return -1;
            }
            else {
                try {
                    int[] node = path.get(path.size() - 1);
                    while (node != null && steps < maxRange + 1) {
                        if (steps < maxRange + 1) {
                            steps += node[2];
                            path.add(pathfind(path, node[0], node[1], x, y));
                            node = path.get(path.size() - 1);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return -1;
                }
                return path.size() - 1;
            }
        }
    }

    //Create the movement interface grid.
    public void createMovementRange() {
        setMovement(owner.charSheet.speed() / 5);
        pathGrid = new Enums.pathTile[map.h][map.w];
        distGrid = new int[map.h][map.w];
        for (int y = 0; y < map.h; y += 1) {
            for (int x = 0; x < map.w; x += 1) {
                pathGrid[y][x] = Enums.pathTile.NULL;
                distGrid[y][x] = findPathDistance(x, y);
                if (distGrid[y][x] > -1) {
                    if (distGrid[y][x] <= move) {
                        pathGrid[y][x] = Enums.pathTile.FREE;
                    } else if (distGrid[y][x] <= maxRange) {
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
                if (owner.map.map[y][x].equals(Enums.tileType.WALL)) {
                    icon = '#';
                }
                else {
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
                }
                if (x == owner.x && y == owner.y) {
                    icon = owner.name.toUpperCase().charAt(0);
                }
                System.out.print(icon + " ");
            }
            System.out.println();
        }
    }

    //Print the map's layout to the terminal.
    public void showDistances() {
        System.out.println("Map : " + this);
        for (int y = 0; y < distGrid.length; y += 1) {
            for (int x = 0; x < distGrid[y].length; x += 1) {
                if (x == owner.x && y == owner.y) {
                    System.out.print(String.format("%2c", ' ') + " ");
                }
                else {
                    int node = distGrid[y][x];
                    if (node > -1) {
                        System.out.print(String.format("%2d", distGrid[y][x]) + " ");
                    } else {
                        System.out.print("NN ");
                    }
                }
            }
            System.out.println();
        }
    }
}