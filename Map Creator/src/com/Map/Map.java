package com.Map;

import com.Entities.Characters.Character;
import com.Entities.Characters.Player;
import com.GUI.Camera;
import com.GUI.GUI;
import com.GUI.System.Background;
import com.methods;

import java.util.ArrayList;
import java.util.HashSet;

public class Map {
    public GUI frame;                                                   //The frame the map is assigned to.
    public String name;                                                 //The map's name.
    public int[][] map;                                                 //The map's terrain layout.
    public boolean[][] visited;                                         //Where the player has visited.
    public HashSet<Character> characterList;                            //The list of all of the map's entities.
    public Background background;                                       //The map's tileset background.
    public Camera camera;                                               //The map's camera.
    public Object[][] enemySpawns = new Object[0][0];                   //The map's spawn pool.
    public Object[][] staticEnemySpawns = new Object[0][0];             //The enemies that will be spawned when the set map initializes.
    public int[][] staticEnemyLocations;                                //The set map's enemy locations.
    public int floor = 0;                                               //The floor of the map.
    public int w;                                                       //The map's dimensions.
    public int h;
    public int th;                                                      //The map's tile dimensions.
    public int tw;
    public WallChecker wallChecker;      //The map's wallChecker that determines terrain autotiling.

    //Map constructor.
    public Map() {
    }
    public Map(String[] map) {
        if (map != null) {
            assignSetMap(map);
        }
        setMapAttributes();
    }
    public Map(String[] map, String background) {
        this(map);
        setBackground(background);
    }

    //Fill in tiles on the minimap as the player encounters them.
    public void setVisited() {
        visited = new boolean[h][w];
        System.out.println(methods.tuple("visited", w, h));
        for (int y = 0; y < h; y += 1) {
            for (int x = 0; x < w; x += 1) {
                visited[y][x] = false;
            }
        }
    }

    //Set the map's frame.
    public void setFrame(GUI frame) {
        this.frame = frame;
    }

    //Set the map's name.
    public void setName(String name) {
        this.name = name;
    }

    //Set the map's enemy spawn pool.
    public void setEnemySpawns(Object ... enemy) {
        if (enemy != null) {
            enemySpawns = new Object[enemy.length / 2][2];
            for (int i = 0; i < enemy.length / 2; i += 1) {
                enemySpawns[i] = new Object[]{enemy[i * 2], enemy[(i * 2) + 1]};
            }
        } else {
            enemySpawns = new Object[0][0];
        }
    }

    public void setEnemyLocations(Object ... enemy) {
        if (enemy != null) {
            staticEnemySpawns = new Object[enemy.length / 4][2];
            staticEnemyLocations = new int[enemy.length / 4][2];
            for (int i = 0; i < enemy.length / 4; i += 1) {
                //com.System.out.println(methods.tuple(enemy[i * 4], enemy[(i * 4) + 1], enemy[(i * 4) + 2], enemy[(i * 4) + 3]));
                staticEnemySpawns[i] = new Object[] { enemy[i * 4], enemy[(i * 4) + 1] };
                staticEnemyLocations[i] = new int[] { (int) enemy[(i * 4) + 2], (int) enemy[(i * 4) + 3] };
            }
        } else {
            staticEnemySpawns = new Object[0][0];
            staticEnemyLocations = new int[0][0];
        }
    }

    //Set the map's background to null or an actual tilesheet.
    public void setBackground() {
        setBackground("");
    }
    public void setBackground(String background) {
        this.background = new Background(background, this);
    }

    //Add a player to the map and align the entity lists to match.
    public void addPlayer(Player player) {
        player.map = this;
        if (player != null) {
            player.setBoundingBox();
        }
        characterList.add(player);
    }

    public void setMap(int[][] map) {
        this.map = map;
        display();
        w = this.map[0].length;
        h = this.map.length;
        setVisited();
    }
    //Set the map's layout and cloud the area in a fog of war.
    public void assignSetMap(String[] map) {
        setMap(parseMap(map));
    }

    //Parse the inputted character map to generate the floor layout.
    public int[][] parseMap(String[] map) {
        int[][] parsedMap = new int[map.length][(map[0].length() + 1) / 2];
        System.out.println(map[0].length() + " " + ((map[0].length() + 1) / 2));
        for (int i = 0; i < parsedMap.length; i += 1) {
            for (int j = 0; j < parsedMap[i].length; j += 1) {
                parsedMap[i][j] = 0;
                char tile = map[i].charAt(j * 2);
                switch (tile) {
                    case '#': parsedMap[i][j] = 1; break;
                    case '-': parsedMap[i][j] = 5; break;
                }
            }
        }
        return parsedMap;
    }

    //Set the map's floor.
    public void setFloor(int floor) {
        this.floor = floor;
    }

    //Set the map's entity lists and backgrounds.
    public void setMapAttributes() {
        this.characterList = new HashSet<>();
        setBackground();
    }

    //Check whether an entity exists at a particular location on the map.
    public Character checkEntityPositions(int x, int y) {
        for (Character character : characterList) {
            if (character != null) {
                if (character.x == x && character.y == y) {
                    return character;
                }
            }
        }
        return null;
    }

    //Add an enemy to the map and align the entity lists to match.
    public void addEnemy(String name, int x, int y) {
        Class<?> enemyClass = EnemyBattlers.battlers.get(name);
        System.out.println("[DBG]: " + enemyClass + " " + enemyClass.getConstructors()[0]);
        try {
            Character enemy = (Character) enemyClass.getConstructors()[0].newInstance(this);
            System.out.println("[DBG]: Enemy " + enemy);
            enemy.teleport(x, y);
            characterList.add(enemy);
            System.out.println("[DBG]: Enemy created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Check if a certain coordinate is a wall.
    public boolean isWall(int x, int y) {
        return isWall(map, x, y);
    }
    public boolean isWall(int[][] map, int x, int y) {
        try {
            if (map[y][x] == 1) {
                return true;
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }
    public boolean isWall(int x1, int y1, int x2, int y2) {
        for (int y = y1; y < y2 + 1; y += 1) {
            for (int x = x1; x < x2 + 1; x += 1) {
                if (!isWall(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    //Check if a certain coordinate is water.
    public boolean isWater(int x, int y) {
        try {
            if (map[y][x] == 5) {
                return true;
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    //Print the map's layout to the terminal.
    public void display() {
        System.out.println("Map : " + this);
        for (int y = 0; y < map.length; y += 1) {
            for (int x = 0; x < map[y].length; x += 1) {
                char icon = ' ';
                switch (map[y][x]) {
                    case 0:
                        icon = ' ';
                        break;      // floor
                    case 1:
                        icon = '#';
                        break;      // wall
                    case 2:
                        icon = '?';
                        break;      // spawning pit
                    case 5:
                        icon = '-';
                        break;      // water
                }
                for (Character character : characterList) {
                    if (x == character.x && y == character.y) {
                        icon = character.name.toUpperCase().charAt(0);
                    }
                }
                System.out.print(icon + " ");
            }
            System.out.println();
        }
    }
}
