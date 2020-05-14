package com.Game;

import com.Connection.Hosts.ClientHost;
import com.Connection.GUI.SessionGUI;
import com.Game.GUI.GUI;

public class main {
    //Run the title menu, passing a window with and height of 1024 and 720.
    public static void main(String ... args) {
        SessionGUI sGui = new SessionGUI("D&D Encounter Simulator", 1024, 720);
    }
    //Run the game, passing a window with and height of 1024 and 720.
    public static GUI main2(ClientHost host) {
        GUI gui = new GUI("D&D Encounter Simulator (ID: " + host.name + ")", 1024, 720, 11, 7, host);
        gui.setVisible(true);
        return gui;
    }
}