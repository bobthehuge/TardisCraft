package net.bobthehuge.tardiscraft;

import com.onarandombox.MultiverseCore.MultiverseCore;
import net.bobthehuge.tardiscraft.commands.LobbyCommand;
import net.bobthehuge.tardiscraft.managers.WarpManager;
import net.bobthehuge.tardiscraft.commands.WarpCommand;
import net.bobthehuge.tardiscraft.listeners.JoinListener;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import net.bobthehuge.tardiscraft.playerinfos.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

public final class Tardiscraft extends JavaPlugin {

    public static final String command_prefix = "tardiscraft";
    public static final MultiverseCore MVC =
            (MultiverseCore) Bukkit.getServer()
                .getPluginManager()
                .getPlugin("Multiverse-Core");

    public static final HashMap<Player, PlayerInfos> PlayersInfos = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Tardiscraft is enabled");
        getServer()
            .getPluginManager()
            .registerEvents(new JoinListener(), this);

        WarpManager.hub.createWarp();
        if (!WarpManager.registerWarp(WarpManager.hub))
            throw new RuntimeException("Can't register Hub in Warps list");

        getServer().getCommandMap().register("warp", new WarpCommand());
        getServer().getCommandMap().register("lobby", new LobbyCommand());

        Bukkit.getLogger().setLevel(Level.WARNING);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MVC.getMVWorldManager().unloadWorld(WarpManager.hub.getWorldName());
        getLogger().info("Tardiscraft is disabled");
    }

    public static boolean CheckPlayerPerm(Player player, String p) {
        String[] tokens = p.split("\\.");

        if (tokens.length == 0 || !tokens[0].equals("tardiscraft"))
            return false;

        PlayerInfos infos = PlayersInfos.get(player);

        if (infos.getRole() == PlayerRole.STANDARD)
            switch (tokens[1]) {
                case "warp": return tokens.length == 4 && tokens[2].equals("ME") &&
                    (tokens[3].equals("hub") || tokens[3].equals("last"));
                default: return false;
            }

        return infos.getRole() == PlayerRole.OP;
    }
}
