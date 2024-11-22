package net.bobthehuge.tardiscraft.commands;

import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.generators.Generator;
import net.bobthehuge.tardiscraft.generators.IslandGenerator;
import net.bobthehuge.tardiscraft.generators.SkyBlockIslandGenerator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class IslandCommand extends Command {
    private static final String COMMAND_PERM_STRING = "tardiscraft.island";
    private static final Random RANDOM = new Random();

    public IslandCommand() {
        super("island", "Generates a SkyBlock island", "/island [px pz xz]", List.of("is"));
    }

    private @NotNull Generator.Context createContext(@NotNull World world, @NotNull Location origin, @NotNull String[] args) throws CommandException {
        if (args.length < 3) {
            return Generator.makeContext(world, origin, RANDOM);
        }

        try {
            int px = Integer.parseInt(args[0]);
            int pz = Integer.parseInt(args[1]);
            boolean xz = Boolean.parseBoolean(args[2]);

            return new Generator.Context(world, px, pz, xz, origin);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid arguments");
        }
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

        if (args.length != 0 && args.length != 3) {
            commandSender.sendMessage(String.format("Usage: %s", getUsage()));
            return false;
        }

        World world = player.getWorld();
        Location origin = player.getLocation();

        try {
            Generator gen = new SkyBlockIslandGenerator();
            gen.generate(createContext(world, origin, args));
        } catch (CommandException e) {
            commandSender.sendMessage(e.getMessage());
            return false;
        }

        return true;
    }
}
