package net.bobthehuge.tardiscraft;

import com.onarandombox.MultiverseCore.MultiverseCore;
import it.unimi.dsi.fastutil.Pair;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.activities.Hub;
import net.bobthehuge.tardiscraft.commands.ReliefIslandCommand;
import net.bobthehuge.tardiscraft.commands.SkyWarsCommand;
import net.bobthehuge.tardiscraft.commands.WarpCommand;
import net.bobthehuge.tardiscraft.commands.IslandCommand;
import net.bobthehuge.tardiscraft.connection.JoinListener;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import net.bobthehuge.tardiscraft.playerinfos.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Tardiscraft extends JavaPlugin {

    public static MultiverseCore MVC =
            (MultiverseCore) Bukkit.getServer()
                .getPluginManager()
                .getPlugin("Multiverse-Core");

    public static Warp Hub = new Hub();
    public static List<Warp> BedwarsInstances = new ArrayList<Warp>();

    public static ArrayList<Warp> Warps = new ArrayList<Warp>();

    public static HashMap<Player, PlayerInfos> PlayersInfos = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Tardiscraft is enabled");
        getServer()
            .getPluginManager()
            .registerEvents(new JoinListener(), this);

        Hub.CreateWarp();

        if (!registerWarp(Hub))
            throw new RuntimeException("Can't register Hub in Warps list");

        getServer().getCommandMap().register("warp", new WarpCommand());
        getServer().getCommandMap().register("island", new IslandCommand());
        getServer().getCommandMap().register("skywars", new SkyWarsCommand());
        getServer().getCommandMap().register("reliefisland", new ReliefIslandCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MVC.getMVWorldManager().unloadWorld(Hub.getWorldName());
        getLogger().info("Tardiscraft is disabled");
    }

    public static boolean checkTCPlayerPerm(Player player, String p) {
        String[] tokens = p.split("\\.");

        Bukkit.getLogger().info(p);

        if (tokens.length != 4 || !tokens[0].equals("tardiscraft"))
            return false;

        if (PlayersInfos.get(player).getRole() == PlayerRole.STANDARD)
            switch (tokens[1]) {
                case "warp": return tokens[2].equals("ME") && tokens[3].equals("hub");
                default: return false;
            }

        if (PlayersInfos.get(player).getRole() == PlayerRole.OP)
            return warpExists(tokens[3]);

        return false;
    }

    public static boolean registerWarp(Warp warp) {
        if (Warps.contains(warp))
            return false;
        Warps.add(warp);
        return true;
    }

    public static boolean unregisterWarp(Warp warp) {
        if (Warps.contains(warp))
            return false;
        Warps.remove(warp);
        return true;
    }

    public static boolean warpExists(String name) {
        return !name.isEmpty() && Warps.stream().anyMatch(w -> {
            return w.getName().equals(name);
        });
    }

    public static boolean warpPlayer(Player p, Warp w)
    {
        if (!Warps.contains(w)) {
            Bukkit.getLogger().warning(String.format("No warp named %s found", w.getName()));
            return false;
        }
        if (Bukkit.getPlayer(p.getUniqueId()) == null) {
            Bukkit.getLogger().warning(String.format("No player named %s found", p.getName()));
            return false;
        }

        PlayerInfos infos = Tardiscraft.PlayersInfos.get(p);

        Warp old = infos.getWarp();
        if (old != null)
            old.PlayerLeave(p);

        w.PlayerJoin(p);

        return true;
    }
}
