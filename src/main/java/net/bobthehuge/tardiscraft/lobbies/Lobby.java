package net.bobthehuge.tardiscraft.lobbies;

import org.bukkit.entity.Player;

import java.util.List;

public interface Lobby {
    public String getName();
    public List<Player> getPlayers();
    public void PlayerJoin(Player player);
    public void PlayerLeave(Player player);
    public void LobbyStart();
    public void LobbyEnd();
}
