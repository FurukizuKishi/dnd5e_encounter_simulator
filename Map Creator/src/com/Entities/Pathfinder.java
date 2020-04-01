package com.Entities;

import com.Map.Map;

public class Pathfinder {
    private int move;
    private int range;
    private int[][] pathGrid;
    private Map map;
    public Pathfinder(int move, int range, Map map) {
        this.move = move;
        this.range = range;
        this.map = map;
    }
    public Pathfinder(int move, Map map) {
        this(move, 1, map);
    }

    public void pathfind(int x, int y) {
    }

    public void createMovementRange() {
        for (int y = 0; y < map.h / map.th; y += 1) {
            for (int x = 0; x < map.w / map.tw; y += 1) {

            }
        }
    }
}
