package net.bobthehuge.tardiscraft.commands;

import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.generators.BedWarsGenerator;
import net.bobthehuge.tardiscraft.generators.Generator;
import net.bobthehuge.tardiscraft.generators.SkyBlockIslandGenerator;
import net.bobthehuge.tardiscraft.generators.SkyWarsGenerator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class SkyWarsCommand extends Command {
    private static final String COMMAND_PERM_STRING = "tardiscraft.island";
    private static final Random RANDOM = new Random();

    public SkyWarsCommand() {
        super("skywars", "Generates a SkyWars map", "/skywars <r> <n>", List.of("sw"));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return false;
        }

        Warp warp = Tardiscraft.PlayersInfos.get(player).getWarp();

        StringBuilder perm = new StringBuilder(COMMAND_PERM_STRING);
        perm.append(".ME.");
        perm.append(warp.getName());

        if (!Tardiscraft.checkTCPlayerPerm(player, perm.toString())) {
            commandSender.sendMessage("You don't have the required permission to use this command.");
            return false;
        }

        if (args.length != 2) {
            commandSender.sendMessage(String.format("Usage: %s", getUsage()));
            return false;
        }

        World world = player.getWorld();
        Location origin = player.getLocation();

        try {
            int radius = Integer.parseInt(args[0]);
            int islands = Integer.parseInt(args[1]);

            Generator gen = new SkyWarsGenerator(radius, islands);
            gen.generate(Generator.makeContext(world, origin, RANDOM));
        } catch (CommandException e) {
            commandSender.sendMessage(e.getMessage());
            return false;
        }

        return true;
    }
}
