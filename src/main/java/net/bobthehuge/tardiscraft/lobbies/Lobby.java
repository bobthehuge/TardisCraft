package net.bobthehuge.tardiscraft.lobbies;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public interface Lobby {
    public String getName();
    public List<Player> getPlayers();
    public boolean playerJoin(Player player);
    public boolean playerLeave(Player player);
    public boolean lobbyStart();
    public void lobbyDelete();
    public void broadcastMessage(String message);
    public void broadcastMessage(Component component);
}

