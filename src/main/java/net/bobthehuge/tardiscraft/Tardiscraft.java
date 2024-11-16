package net.bobthehuge.tardiscraft;

import com.onarandombox.MultiverseCore.MultiverseCore;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.activities.Hub;
import net.bobthehuge.tardiscraft.connection.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Tardiscraft extends JavaPlugin {

    public static MultiverseCore MVC =
            (MultiverseCore) Bukkit.getServer()
                .getPluginManager()
                .getPlugin("Multiverse-Core");

    public static Warp Hub = new Hub();

    public static HashMap<Player, Warp> PlayerLocations = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Tardiscraft is enabled");
        getServer()
            .getPluginManager()
            .registerEvents(new JoinListener(), this);

        // mvcore.getMVWorldManager().loadWorld("creative");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MVC.getMVWorldManager().unloadWorld("creative");
        getLogger().info("Tardiscraft is disabled");
    }
}
