package com.Game.GUI;

import com.Connection.Hosts.ClientHost;
import com.Game.Entities.Characters.CharacterModel;
import com.Game.Entities.Characters.PlayerModel;
import com.Game.GUI.System.Fonts;
import com.Game.GUI.System.RoomLinker;
import com.Game.GUI.System.Transitions.Transition;
import com.Game.Map.Map;
import com.Game.System.HasFile;
import com.Game.System.InputMethods.*;
import com.Game.System.Text.TextStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GUI extends JFrame {
    public String title;                                                    //The game's title, shown on the top bar of the window.
    public int w;                                                           //The dimensions of the screen and camera.
    public int h;
    public int wCam;
    public int hCam;
    public ClientHost host;                                                 //The client host where the GUI sends and receives commands.
    public HUD hud;                                                         //The game's HUD.
    public Camera camera;                                                   //The game's camera.
    public HashMap<String, PlayerModel> players = new HashMap<>();          //A list of all of the game's players.
    public ArrayList<Map> rooms = new ArrayList<>();                        //A list of all of the game's rooms.
    public ArrayList<Transition> transitions = new ArrayList<>();           //A list of the game's transitions.
    public RoomLinker roomLinker = new RoomLinker(this);            //The room linker that allows for transitions between rooms.
    public HashMap<String, TextStore> textStores = new HashMap<>();         //A map storing all of the game's floating text.
    public HasFile imageSelector = new HasFile(this);               //The image selector for the game.
    private ArrayList<SubFrame> subframes = new ArrayList<>();             //A list of all of the gui's subframes that close when the gui does.
    private Dimension monitorSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    //The GUI constructor. Handles the initial setup of the game, creating fonts, enemy definitions, and setting the window's characteristics.
    public GUI(String title, int width, int height, int cameraWidth, int cameraHeight, ClientHost host) {
        this.host = host;
        textStores.put("damage", new TextStore());
        Fonts.addFont("stonehen.ttf", Fonts.font.TEXT, 30);
        Fonts.addFont("partybusiness.ttf", Fonts.font.DAMAGE, 20);
        Fonts.addFont("DUNGRG__.TTF", Fonts.font.TITLE, 120);
        Fonts.addFont("DUNGRG__.TTF", Fonts.font.SUBTITLE, 60);
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
                        "# # # # # #Nv # # # # # # # # # # # # # # # # #"}, "Cave", players, null);

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
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(title);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                for (int i = 0; i < subframes.size(); i += 1) {
                    subframes.get(0).dispatchEvent(new WindowEvent(subframes.get(0), WindowEvent.WINDOW_CLOSING));
                }
                host.endThread();
            }
        });

        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                GUI gui = (GUI) e.getSource();
                gui.setLocation(0, 0);
                host.frame.setLocation(gui.monitorSize.width - host.frame.w, 0);
                host.frame.setState(gui.getState());
                for (int i = 0; i < subframes.size(); i += 1) {
                    subframes.get(0).setState(gui.getState());
                }
            }
        });

        roomLinker.unlinkRoom();
        roomLinker.startRoomSwitch(rooms.get(0));
        players.get("Akuma").teleport(6, 4);
        players.get("Kael").teleport(5, 5);
        camera.map.display();

        this.addMouseListener(new MouseGridInput(this));
        this.addMouseMotionListener(new MouseGridInput(this));

        //EditorFrame editor = new EditorFrame(this);
        setResizable(false);
        repaint();
    }

    //Add a subframe.
    public void addSubframe(SubFrame frame) {
        subframes.add(frame);
    }
    //Delete a subframe.
    public void removeSubframe(SubFrame frame) {
        subframes.remove(frame);
    }
    //Get the index of a subframe.
    public int getSubframe(SubFrame frame) {
        return subframes.indexOf(frame);
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
        //players.get(name).setSprite(imageSelector.selectImage("files/images/"));
    }
}