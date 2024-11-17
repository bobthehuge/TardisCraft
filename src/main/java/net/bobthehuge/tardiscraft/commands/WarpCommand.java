package net.bobthehuge.tardiscraft.commands;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.managers.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.bobthehuge.tardiscraft.Tardiscraft.CheckPlayerPerm;
import static net.bobthehuge.tardiscraft.Tardiscraft.PlayersInfos;

public class WarpCommand extends Command {

    private final String commandPermString = "tardiscraft.warp";

    public WarpCommand() {
        super("warp", "warps user to destination", "/warp <user?> <warp>", List.of());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (args.length > 2) return false;

        if (sender instanceof Player) {
            Player p1 = (Player) sender;

            if (args.length == 1 && CheckPlayerPerm(p1, commandPermString + ".ME." + args[0])) {
                Warp dst = PlayersInfos.get(p1).getWarp();

                if (!args[0].equals("last"))
                    dst = WarpManager.warps.stream().filter(w -> w.getName().equals(args[0])).findAny().orElse(null);

                return WarpManager.getAccessibleWarps(p1).contains(dst) && WarpManager.warpPlayer(p1, dst);
            }

            if (args.length == 2 && CheckPlayerPerm(p1, commandPermString + ".OTHER." + args[1])) {
                Player p2 = Bukkit.getPlayer(args[0]);
                Warp dst = PlayersInfos.get(p2).getWarp();

                if (!args[1].equals("last"))
                    dst =WarpManager.warps.stream().filter(w -> w.getName().equals(args[1])).findAny().orElse(null);

                return WarpManager.getAccessibleWarps(p1).contains(dst) && p2 != null && WarpManager.warpPlayer(p2, dst);
            }
        } else {
            if (args.length != 2) return false;

            Player p2 = Bukkit.getPlayer(args[0]);
            Warp dst = PlayersInfos.get(p2).getWarp();

            if (!args[1].equals("last"))
                dst = WarpManager.warps.stream().filter(w -> w.getName().equals(args[1])).findAny().orElse(null);

            return p2 != null && WarpManager.warpPlayer(p2, dst);
        }

        return false;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(args != null, "Arguments cannot be null");
        Preconditions.checkArgument(alias != null, "Alias cannot be null");
        if (args.length != 0 && sender.getServer().suggestPlayerNamesWhenNullTabCompletions()) {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player)sender : null;
            ArrayList<String> matchedPlayers = new ArrayList();
            Iterator var8 = sender.getServer().getOnlinePlayers().iterator();

            while(true) {
                Player player;
                String name;
                do {
                    if (!var8.hasNext()) {
                        Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);

                        //if ()

                        return matchedPlayers;
                    }

                    player = (Player)var8.next();
                    name = player.getName();
                } while(senderPlayer != null && !senderPlayer.canSee(player));

                if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
                    matchedPlayers.add(name);
                }
            }
        } else {
            return ImmutableList.of();
        }
    }
}
