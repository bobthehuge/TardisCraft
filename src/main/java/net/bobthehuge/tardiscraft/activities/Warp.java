package net.bobthehuge.tardiscraft.activities;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.entity.Player;

import java.util.List;

public interface Warp {
    public String getName();
    public String getWorldName();
    public WarpType getWarpType();
    public MultiverseWorld getWorld();
    public List<Player> getPlayers();
    public WarpState getWarpState();
    public void createWarp();
    public void playerJoin(Player player);
    public void playerLeave(Player player);
    public void startActivity();
    public void stopActivity();
}