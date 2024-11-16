package net.bobthehuge.tardiscraft.commands;

import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WarpCommand extends Command {

    private final String commandPermString = "tardiscraft.warp";

    public WarpCommand() {
        super("warp", "warps user to destination", "/warp <user?> <warp>", List.of());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p1 = (Player) sender;

            if (args.length > 2) return true;

            if (args.length == 1 && Tardiscraft.checkTCPlayerPerm(p1, commandPermString + ".ME." + args[0])) {
                Warp dst = Tardiscraft.Warps.stream().filter(w -> w.getName().equals(args[0])).findAny().orElse(Tardiscraft.Hub);

                Bukkit.getLogger().info(String.format("%s tried to warp to %s", p1.getName(), args[0]));

                Tardiscraft.warpPlayer(p1, dst);
            }

            if (args.length == 2 && Tardiscraft.checkTCPlayerPerm(p1, commandPermString + ".OTHER." + args[1])) {
                Warp dst = Tardiscraft.Warps.stream().filter(w -> w.getName().equals(args[1])).findAny().orElse(Tardiscraft.Hub);
                Player p2 = Bukkit.getPlayer(args[0]);

                if (p2 == null)
                    return true;

                Bukkit.getLogger().info(String.format("%s tried to warp %s to %s", p1.getName(), p2.getName(), args[1]));
                Tardiscraft.warpPlayer(p2, dst);
            }
        } else {
            Bukkit.getLogger().info("not instance of Player");
        }
        return true;
    }
}
