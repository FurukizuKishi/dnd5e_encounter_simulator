package com.Game.GUI.System;

import com.Game.Entities.Characters.PlayerModel;
import com.Game.GUI.GUI;
import com.Game.Map.Map;

public class RoomLinker {
    public GUI frame;

    public RoomLinker(GUI frame) {
        this.frame = frame;
    }

    //Prepare the room to be switched, pausing game flow and breaking the necessary map links. Transition should only be set to true if you wish to
    //move to a new room. Otherwise, use unlinkRoom() if you don't want to pause game flow.
    public void startRoomSwitch(Map room) {
        if (room != null) {
            room.display();
        }
        unlinkRoom();
        if (room != null) {
            linkRoom(room);
            finishRoomSwitch(room);
        }
    }

    //Breaks all linkages between a room and transient objects that can leave it.
    public void unlinkRoom() {
        for (Map r : frame.rooms) {
            r.characterList.removeAll(frame.players.values());
            r.camera = null;
        }
        for (PlayerModel player : frame.players.values()) {
            player.setMap(null);
        }
        if (frame.camera != null) {
            frame.camera.setMap(null);
        }
    }

    //Create new links between the player and camera to a new map.
    public void linkRoom(Map room) {
        room.characterList.addAll(frame.players.values());
        frame.camera.setMap(room);
        for (PlayerModel player : frame.players.values()) {
            player.setMap(room);
        }
    }

    //Close up a room transition, restarting the game flow and allocating a new time limit for the floor.
    public void finishRoomSwitch(Map room) {

    }
}
