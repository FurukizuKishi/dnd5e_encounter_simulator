package com.GUI;

import com.Entities.Characters.CharacterModel;
import com.Entities.Characters.PlayerModel;
import com.GUI.System.Fonts;
import com.GUI.System.RoomLinker;
import com.GUI.System.Transitions.Transition;
import com.Map.Map;
import com.System.Enums;
import com.System.HasImage;
import com.System.InputMethods.MouseInput;
import com.System.Text.FloatingText;
import com.System.Text.TextStore;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GUI extends JFrame {
    public String title;                                                    //The game's title, shown on the top bar of the window.
    public int w;                                                           //The dimensions of the screen and camera.
    public int h;
    public int wCam;
    public int hCam;
    public HUD hud;                                                         //The game's HUD.
    public Camera camera;                                                   //The game's camera.
    public HashMap<String, PlayerModel> players = new HashMap<>();          //A list of all of the game's players.
    public ArrayList<Map> rooms = new ArrayList<>();                        //A list of all of the game's rooms.
    public ArrayList<Transition> transitions = new ArrayList<>();           //A list of the game's transitions.
    public RoomLinker roomLinker = new RoomLinker(this);            //The room linker that allows for transitions between rooms.
    public HashMap<String, TextStore> textStores = new HashMap<>();         //A map storing all of the game's floating text.
    public HasImage imageSelector = new HasImage(this);             //The image selector for the game.

    //The GUI constructor. Handles the initial setup of the game, creating fonts, enemy definitions, and setting the window's characteristics.
    public GUI(String title, int width, int height, int cameraWidth, int cameraHeight) {
        textStores.put("damage", new TextStore());
        Fonts.addFont("fonts/stonehen.ttf", Fonts.font.TEXT, 30);
        Fonts.addFont("fonts/partybusiness.ttf", Fonts.font.DAMAGE, 20);
        Fonts.addFont("fonts/DUNGRG__.TTF", Fonts.font.TITLE, 120);
        Fonts.addFont("fonts/DUNGRG__.TTF", Fonts.font.SUBTITLE, 60);
        this.title = title;
        w = width;
        h = height;
        wCam = cameraWidth;
        hCam = cameraHeight;

        createSetRoom("Boss Fight", new String[]
                       {"# # # # # # # # # # # # # # # # # # # # # # # #",
                        "# # - - - - - - - - - # # # - - - - - - - # # #",
                        "# - - -           - - - - - - -       - - - # #",
                        "#               # #   - - -               - # #",
                        "#             # #       -                     #",
                        "#             # #                           # #",
                        "# #                   # # # #           # # # #",
                        "# # # # # #Nv # # # # # # # # # # # # # # # # #"}, null, players, null);

        createPlayer("Akuma", 8, 30, 12, 10, 18, 14);
        players.get("Akuma").charSheet.assignMaxHealth(154);
        players.get("Akuma").charSheet.setHealth(128);
        players.get("Akuma").setFrame(this);
        createPlayer("Kael", 14, 19, 16, 14, 10, 14);
        players.get("Kael").charSheet.assignMaxHealth(65);
        players.get("Kael").charSheet.setHealth(59);
        players.get("Kael").setFrame(this);

        hud = new HUD(w, h, null);
        camera = new Camera(this, wCam, hCam, hud, rooms.get(0));
        w = camera.sw;
        h = camera.sh;
        add(camera, BorderLayout.CENTER);

        //Set the window size to the class attributes w and h.
        setSize(w, h);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(title);

        roomLinker.unlinkRoom();
        roomLinker.startRoomSwitch(rooms.get(0));
        players.get("Akuma").teleport(6, 4);
        players.get("Kael").teleport(5, 5);
        camera.map.display();

        this.addMouseListener(new MouseInput(this));

        repaint();
    }

    //Create a new room, setting its attributes, placing the player within it and spawning its enemies.
    public void createRoom(String name, String background, HashMap<String, PlayerModel> players, ArrayList<CharacterModel> enemies) {
        Map room = rooms.get(rooms.size() - 1);
        room.setFrame(this);
        room.characterList.addAll(players.values());
        if (enemies != null) {
            room.characterList.addAll(enemies);
        }
        Collections.sort(room.characterList);
        room.setName(name);
        room.setBackground(background);
        room.display();
    }
    //Create a room with a predetermined map.
    public void createSetRoom(String name, String[] map, String background, HashMap<String, PlayerModel> players, ArrayList<CharacterModel> enemies) {
        rooms.add(new Map(map));
        createRoom(name, background, players, enemies);
    }

    //Create a new player character.
    public void createPlayer(String name, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        players.put(name, new PlayerModel(name, strength, dexterity, constitution, intelligence, wisdom, charisma));
        players.get(name).setSprite(imageSelector.selectFile("files/images/"));
    }

    //Align text using its width in pixels. Justifications include LEFT, MIDDLE and RIGHT.
    public void alignFloatingText(FloatingText text, Enums.alignment justification) {
        text.alignmentOffset = getNewTextAlignment(text.text, justification);
    }
    public int getNewTextAlignment(String str, Enums.alignment justification) {
        return getNewTextAlignment(str, justification, getFont());
    }
    public int getNewTextAlignment(String str, Enums.alignment justification, Fonts.font font) {
        return getNewTextAlignment(str, justification, Fonts.fonts.get(font));
    }
    public int getNewTextAlignment(String str, Enums.alignment justification, Font font) {
        FontMetrics fm = getFontMetrics(font);
        int fontWidth = fm.stringWidth(str);
        if (justification == Enums.alignment.LEFT) {
            return 0;
        }
        else if (justification == Enums.alignment.MIDDLE) {
            return fontWidth / 2;
        }
        else if (justification == Enums.alignment.RIGHT) {
            return fontWidth;
        }
        return 0;
    }
}