package com.Map;

import com.Entities.Characters.CharacterModel;
import com.GUI.Camera;
import com.GUI.GUI;
import com.GUI.System.Background;
import com.System.Enums;
import com.methods;

import java.util.ArrayList;
import java.util.HashSet;

public class Map {
    public GUI frame;                                                   //The frame the map is assigned to.
    public String name;                                                 //The map's name.
    public Enums.tileType[][] map;                                      //The map's terrain layout.
    public boolean[][] visited;                                         //Where the player has visited.
    public ArrayList<CharacterModel> characterList = new ArrayList<>(); //The list of all of the map's entities.
    public boolean encounter = false;                                   //Whether there is an encounter running on this map.
    public int turn = 1;                                                //The current turn in the round.
    public int round = 1;                                               //The current round in the encounter.
    private boolean editing = false;                                    //Whether the map is being edited.
    public Background background;                                       //The map's tileset background.
    public Camera camera;                                               //The map's camera.
    public int w;                                                       //The map's dimensions.
    public int h;
    public int th = 24;                                                 //The map's tile dimensions.
    public int tw = 24;
    public WallChecker wallChecker;                                     //The map's wallChecker that determines terrain autotiling.

    //Map constructor.
    public Map() {
    }
    public Map(String[] map) {
        if (map != null) {
            assignSetMap(map);
        }
        setMapAttributes();
    }
    public Map(String[] map, Background background) {
        this(map);
        this.background = background;
    }
    public Map(String[] map, String background) {
        this(map);
        setBackground(background);
    }
    public Map(Enums.tileType[][] map) {
        if (map != null) {
            setMap(map);
        }
        setMapAttributes();
    }
    public Map(Enums.tileType[][] map, Background background) {
        this(map);
        this.background = background;
    }
    public Map(Enums.tileType[][] map, String background) {
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

    //Set the map's background to null or an actual tilesheet.
    public void setBackground() {
        setBackground("");
    }
    public void setBackground(String background) {
        this.wallChecker = new WallChecker(this);
        this.background = new Background(background, this);
    }

    //Add a player to the map and align the entity lists to match.
    public void addCharacter(CharacterModel character) {
        character.map = this;
        character.setBoundingBox();
        characterList.add(character);
    }

    public void setMap(Enums.tileType[][] map) {
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
    public Enums.tileType[][] parseMap(String[] map) {
        Enums.tileType[][] parsedMap = new Enums.tileType[map.length][(map[0].length() + 1) / 2];
        System.out.println(map[0].length() + " " + ((map[0].length() + 1) / 2));
        for (int i = 0; i < parsedMap.length; i += 1) {
            for (int j = 0; j < parsedMap[i].length; j += 1) {
                parsedMap[i][j] = Enums.tileType.FLOOR;
                char tile = map[i].charAt(j * 2);
                switch (tile) {
                    case '#': parsedMap[i][j] = Enums.tileType.WALL; break;
                    case '-': parsedMap[i][j] = Enums.tileType.DIFFICULT_TERRAIN; break;
                    case '=': parsedMap[i][j] = Enums.tileType.WATER; break;
                }
            }
        }
        return parsedMap;
    }

    //Set the map's entity lists and backgrounds.
    public void setMapAttributes() {
        this.characterList = new ArrayList<>();
        setBackground();
    }

    public void setEditing(boolean value) {
        editing = value;
    }
    public boolean canEdit() {
        return editing;
    }

    //Check whether an entity exists at a particular location on the map.
    public CharacterModel checkEntityPositions(int x, int y) {
        for (CharacterModel character : characterList) {
            if (character != null) {
                if (character.x == x && character.y == y) {
                    return character;
                }
            }
        }
        return null;
    }

    //Check if a certain coordinate is a wall.
    public boolean isWall(int x, int y) {
        return isWall(map, x, y);
    }
    public boolean isWall(Enums.tileType[][] map, int x, int y) {
        try {
            if (map[y][x] == Enums.tileType.WALL) {
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

    //Check if a certain coordinate is difficult terrain.
    public boolean isDifficultTerrain(int x, int y) {
        try {
            if (map[y][x] == Enums.tileType.DIFFICULT_TERRAIN) {
                return true;
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    //Check if a certain coordinate is water.
    public boolean isWater(int x, int y) {
        try {
            if (map[y][x] == Enums.tileType.WATER) {
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
                    case FLOOR:
                        icon = ' ';
                        break;      // floor
                    case WALL:
                        icon = '#';
                        break;      // wall
                    case DIFFICULT_TERRAIN:
                        icon = '-';
                        break;      // difficult terrain
                    case WATER:
                        icon = '=';
                        break;      // water
                }
                for (CharacterModel character : characterList) {
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
