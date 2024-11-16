package net.bobthehuge.tardiscraft.activities;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public interface Warp {
    public String getName();
    public String getWorldName();
    public WarpType getWarpType();
    public MultiverseWorld getWorld();
    public List<Player> getPlayers();
    public WarpState getWarpState();
    public void CreateWarp();
    public void PlayerJoin(Player player);
    public void PlayerLeave(Player player);
    public void StartActivity();
    public void StopActivity();
}