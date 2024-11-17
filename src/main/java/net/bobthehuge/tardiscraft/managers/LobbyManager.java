package net.bobthehuge.tardiscraft.managers;

import net.bobthehuge.tardiscraft.lobbies.Lobby;

import java.util.ArrayList;

public class LobbyManager {
    public static final ArrayList<Lobby> lobbies = new ArrayList<>();

    public static boolean registerLobby(Lobby lobby) {
        if (lobbies.contains(lobby))
            return false;
        lobbies.add(lobby);
        return true;
    }

    public static boolean unregisterLobby(Lobby lobby) {
        if (!lobbies.contains(lobby))
            return false;
        lobbies.remove(lobby);
        return true;
    }
}
