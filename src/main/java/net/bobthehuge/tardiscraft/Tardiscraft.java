package net.bobthehuge.tardiscraft;

import com.onarandombox.MultiverseCore.MultiverseCore;
import net.bobthehuge.tardiscraft.listeners.JoinListener;
import net.bobthehuge.tardiscraft.listeners.LeaveListener;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public final class Tardiscraft extends JavaPlugin {
    public static final String commandPrefix = "tardiscraft";

    @NonNull
    public static final MultiverseCore MVC =
            (MultiverseCore) Objects.requireNonNull(Bukkit.getServer()
                .getPluginManager()
                .getPlugin("Multiverse-Core"));

    public static final HashMap<Player, PlayerInfos> PlayersInfos = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Tardiscraft is enabled");

        getServer()
            .getPluginManager()
            .registerEvents(new JoinListener(), this);
        getServer()
            .getPluginManager()
            .registerEvents(new LeaveListener(), this);

        Bukkit.getLogger().setLevel(Level.WARNING);
    }

    @Override
    public void onDisable() {
        getLogger().info("Tardiscraft is disabled");
    }
}
