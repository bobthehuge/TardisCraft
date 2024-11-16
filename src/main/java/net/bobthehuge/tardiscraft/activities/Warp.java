package net.bobthehuge.tardiscraft.activities;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public interface Warp {
    public List<Player> players = List.of();
    public String world_name = "";
    public World world = null;
    
    public void PlayerJoin(Player player);
    public void PlayerLeave(Player player);
}