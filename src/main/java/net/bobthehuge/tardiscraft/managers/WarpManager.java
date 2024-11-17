package net.bobthehuge.tardiscraft.managers;

import com.google.common.collect.ImmutableList;
import com.onarandombox.MultiverseCore.destination.ExactDestination;
import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.activities.Hub;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import net.bobthehuge.tardiscraft.playerinfos.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.bobthehuge.tardiscraft.Tardiscraft.PlayersInfos;

public class WarpManager {
    public static final Warp hub = new Hub();
    public static final ArrayList<Warp> warps = new ArrayList<>();
    public static final ArrayList<Warp> bedwars = new ArrayList<>();

    public static boolean warpExists(String name) {
        return !name.isEmpty() && warps.stream().anyMatch(w -> {
            return w.getName().equals(name);
        });
    }

    public static List<Warp> getAccessibleWarps(Player player)
    {
        PlayerInfos infos = PlayersInfos.get(player);

        if (infos.getRole() == PlayerRole.STANDARD) {
            List<Warp> result = new ArrayList<>();
            result.add(hub);
            return result;
        }

        if (infos.getRole() == PlayerRole.OP)
            return warps;

        return ImmutableList.of();
    }

    public static boolean registerWarp(Warp warp) {
        if (warps.contains(warp))
            return false;
        warps.add(warp);
        return true;
    }

    public static boolean unregisterWarp(Warp warp) {
        if (!warps.contains(warp))
            return false;
        warps.remove(warp);
        return true;
    }

    public static boolean warpPlayer(Player p, Warp w)
    {
        if (w == null) {
            return false;
        }

        if (!warps.contains(w)) {
            Bukkit.getLogger().warning(String.format("No warp named %s found", w.getName()));
            return false;
        }
        if (Bukkit.getPlayer(p.getUniqueId()) == null) {
            Bukkit.getLogger().warning(String.format("No player named %s found", p.getName()));
            return false;
        }

        PlayerInfos infos = Tardiscraft.PlayersInfos.get(p);
        Warp old = infos.getWarp();

        if (old == w) {
            Bukkit.getLogger().info("Warp: Player already in warp");
            return false;
        }

        if (old != null)
            old.playerLeave(p);

        ExactDestination dst = new ExactDestination();
        dst.setDestination(w.getWorld().getSpawnLocation());

        Tardiscraft.MVC.getSafeTTeleporter().safelyTeleport(
            Bukkit.getConsoleSender(),
            p,
            dst
        );

        w.playerJoin(p);
        return true;
    }
}
