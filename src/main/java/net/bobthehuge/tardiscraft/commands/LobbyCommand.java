package net.bobthehuge.tardiscraft.commands;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.activities.WarpType;
import net.bobthehuge.tardiscraft.lobbies.BedwarsLobby;
import net.bobthehuge.tardiscraft.lobbies.Lobby;
import net.bobthehuge.tardiscraft.managers.LobbyManager;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static net.bobthehuge.tardiscraft.Tardiscraft.PlayersInfos;

public class LobbyCommand extends Command {
    private final String commandPermString = "tardiscraft.lobby";

    public LobbyCommand() {
        super("lobby", "lobby management", "/lobby <action>", List.of());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        boolean isPlayer = sender instanceof Player;

        Player p = isPlayer ? (Player) sender : null;
        PlayerInfos infos = isPlayer ? PlayersInfos.get(p) : null;

        if (isPlayer && !p.isOp()) return false;

        if (args.length == 1) {
            switch (args[0]) {
                case "help" -> {
                    sender.sendMessage(Component.text(
                            super.usageMessage + "\navailable: help list create delete join leave start kick add",
                            NamedTextColor.YELLOW
                        )
                    );
                    return true;
                }
                case "list" -> {
                    String msg = LobbyManager.lobbies.stream().map(Lobby::getName).collect(Collectors.joining(", "));
                    sender.sendMessage(Component.text("Active Lobbies: " + msg, NamedTextColor.GREEN));
                    return true;
                }
                case "leave" -> {
                    if (!isPlayer) {
                        sender.sendMessage("Lobby: You are not a player");
                        return false;
                    }
                    Lobby curr = infos.getLobby();
                    if (curr != null) {
                        curr.playerLeave(p);
                        sender.sendMessage(Component.text(String.format("Lobby: You left lobby %s", curr.getName()), NamedTextColor.YELLOW));
                        return true;
                    } else {
                        sender.sendMessage(Component.text("Lobby: Your are not currently in a lobby", NamedTextColor.RED));
                        return false;
                    }
                }
                default -> {
                    sender.sendMessage(Component.text("Lobby: Invalid lobby subcommand", NamedTextColor.RED));
                    return false;
                }
            }
        }
        else if (args.length == 2) {
            Lobby found = LobbyManager.lobbies.stream().filter(l -> l.getName().equals(args[1])).findAny().orElse(null);
            switch (args[0]) {
                case "join" -> {
                    if (!isPlayer) {
                        sender.sendMessage("Lobby: You are not a player");
                        return false;
                    }
                    if (found != null) {
                        Lobby curr = infos.getLobby();
                        if (curr != null)
                            curr.playerLeave(p);

                        if (found.playerJoin(p)) {
                            return true;
                        } else {
                            sender.sendMessage(Component.text(String.format("Lobby: Can't join lobby %s", args[1]), NamedTextColor.RED));
                            return false;
                        }
                    } else {
                        sender.sendMessage(Component.text(String.format("Lobby: Can't find lobby %s", args[1]), NamedTextColor.RED));
                        return false;
                    }
                }
                case "delete" -> {
                    if (found != null) {
                        found.lobbyDelete();
                        sender.sendMessage(Component.text(String.format("Lobby: Deleted lobby %s", args[1]), NamedTextColor.YELLOW));
                        return true;
                    } else {
                        sender.sendMessage(Component.text(String.format("Lobby: Can't find lobby %s", args[1]), NamedTextColor.RED));
                        return false;
                    }
                }
                case "start" -> {
                    if (found != null) {
                        if (found.lobbyStart()) {
                            sender.sendMessage(Component.text(String.format("Lobby: Started lobby %s", args[1]), NamedTextColor.YELLOW));
                            return true;
                        } else {
                            sender.sendMessage(Component.text(String.format("Lobby: Can't start lobby %s", args[1]), NamedTextColor.RED));
                            return false;
                        }
                    } else {
                        sender.sendMessage(Component.text(String.format("Lobby: Can't find lobby %s", args[1]), NamedTextColor.RED));
                        return false;
                    }
                }
                default -> {
                    sender.sendMessage(Component.text("Lobby: Invalid lobby subcommand", NamedTextColor.RED));
                    return false;
                }
            }
        }
        else if (args.length == 3) {
            Lobby found = LobbyManager.lobbies.stream().filter(l -> l.getName().equals(args[1])).findAny().orElse(null);
            Player p2 = Bukkit.getPlayer(args[2]);

            if (args[0].equals("add")) {
                if (found != null && p2 != null) {
                    if (found.playerJoin(p2)) {
                        sender.sendMessage(Component.text(String.format("Lobby: added %s to lobby %s", args[2], args[1]), NamedTextColor.YELLOW));
                        return true;
                    } else {
                        sender.sendMessage(Component.text(String.format("Lobby: Can't add %s to lobby %s", args[2], args[1]), NamedTextColor.RED));
                        return false;
                    }
                }
                else if (found == null) {
                    sender.sendMessage(Component.text(String.format("Lobby: Can't find lobby %s", args[1]), NamedTextColor.RED));
                    return false;
                }
                else {
                    sender.sendMessage(Component.text(String.format("Lobby: Can't find player %s", args[2]), NamedTextColor.RED));
                    return false;
                }
            }
            else if (args[0].equals("kick")) {
                if (found != null && p2 != null) {
                    if (found.playerLeave(p2)) {
                        sender.sendMessage(Component.text(String.format("Lobby: Kicked %s from lobby %s", args[2], args[1]), NamedTextColor.YELLOW));
                        return true;
                    } else {
                        sender.sendMessage(Component.text(String.format("Lobby: Can't kick %s from lobby %s", args[2], args[1]), NamedTextColor.YELLOW));
                        return false;
                    }
                }
                else if (found == null) {
                    sender.sendMessage(Component.text(String.format("Lobby: Can't find lobby %s", args[1]), NamedTextColor.RED));
                    return false;
                }
                else {
                    sender.sendMessage(Component.text(String.format("Lobby: Can't find player %s", args[2]), NamedTextColor.RED));
                    return false;
                }
            }
            else {
                sender.sendMessage(Component.text("Lobby: Invalid lobby subcommand", NamedTextColor.RED));
            }
        }
        else if (args.length == 6 && args[0].equals("create")) {
            // /lobby create bedwars bed1 2 8 SandCastle

            WarpType type = WarpType.getWarpType(args[1]);
            int min = 0;
            int max = 0;

            try {
                min = Integer.parseInt(args[3]);
                max = Integer.parseInt(args[4]);

                if (min > max)
                {
                    max ^= min;
                    min ^= max;
                    max ^= min;
                }

            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Lobby: Invalid min or max player", NamedTextColor.RED));
                return false;
            }

            if (type == null) {
                sender.sendMessage(Component.text(String.format("Lobby: Can't find Warp type %s", args[1]), NamedTextColor.RED));
                return false;
            } else if (!Tardiscraft.MVC.getMVWorldManager().isMVWorld(args[5])) {
                sender.sendMessage(Component.text(String.format("Lobby: Can't find world %s", args[5]), NamedTextColor.RED));
                return false;
            } else {
                Lobby lobby = switch (type) {
                    case BEDWARS -> new BedwarsLobby(args[2], min, max, args[5]);
                    default -> null;
                };

                if (lobby == null) {
                    sender.sendMessage(Component.text(String.format("Lobby: Can't create lobby %s", args[5]), NamedTextColor.RED));
                }

                if (!LobbyManager.registerLobby(lobby)) {
                    sender.sendMessage(Component.text(String.format("Lobby: Can't register lobby %s", args[5]), NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.text(String.format("Lobby: Created lobby %s on %s", args[2], args[5]), NamedTextColor.GOLD));
                    Bukkit.getLogger().info(String.format("Lobby: %s created %s", sender.getName(), args[2]));
                }
            }
        }
        else {
            sender.sendMessage(Component.text("Lobby: Invalid lobby subcommand", NamedTextColor.RED));
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
