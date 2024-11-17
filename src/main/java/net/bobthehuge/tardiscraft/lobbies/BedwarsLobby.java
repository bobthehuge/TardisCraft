package net.bobthehuge.tardiscraft.lobbies;

import net.bobthehuge.tardiscraft.activities.Bedwars;
import net.bobthehuge.tardiscraft.activities.WarpType;
import net.bobthehuge.tardiscraft.managers.LobbyManager;
import net.bobthehuge.tardiscraft.managers.WarpManager;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.bobthehuge.tardiscraft.Tardiscraft.PlayersInfos;

public class BedwarsLobby implements Lobby {

    private final WarpType warpType = WarpType.BEDWARS;
    private final String mapName;
    private List<Player> players = new ArrayList<Player>();
    private final String lobbyName;
    private final int minPlayers;
    private final int maxPlayers;

    public BedwarsLobby(String name, int minPlayers, int maxPlayers, String mapName) {
        this.lobbyName = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.mapName = mapName;
    }

    @Override
    public String getName() {
        return lobbyName;
    }

    @Override
    public List<Player> getPlayers() {
        return List.of();
    }

    @Override
    public boolean playerJoin(Player player) {
        if (players.size() >= maxPlayers) {
            return false;
        }

        this.players.add(player);
        PlayersInfos.get(player).setLobby(this);

        broadcastMessage(
            Component.text(player.getName()).append(
                Component.text(" has joined the lobby (", NamedTextColor.YELLOW).append(
                    Component.text(players.size()).color(
                        players.size() >= minPlayers ? NamedTextColor.GREEN : NamedTextColor.RED
                    ).append(
                        Component.text("/").color(NamedTextColor.YELLOW).append(
                            Component.text(maxPlayers).color(
                                players.size() >= minPlayers ? NamedTextColor.GREEN : NamedTextColor.RED
                            ).append(
                                Component.text(")").color(NamedTextColor.YELLOW)
                            )
                        )
                    )
                )
            )
        );

        return true;
    }

    @Override
    public boolean playerLeave(Player player) {
        if (!this.players.contains(player)) {
            return false;
        }

        this.players.remove(player);
        PlayersInfos.get(player).setLobby(null);
        broadcastMessage(
            Component.text(player.getName()).append(
                Component.text(" has left the lobby (", NamedTextColor.YELLOW).append(
                    Component.text(players.size()).color(
                        players.size() >= minPlayers ? NamedTextColor.GREEN : NamedTextColor.RED
                    ).append(
                        Component.text("/").color(NamedTextColor.YELLOW).append(
                            Component.text(maxPlayers).color(
                                players.size() >= minPlayers ? NamedTextColor.GREEN : NamedTextColor.RED
                            ).append(
                                Component.text(")").color(NamedTextColor.YELLOW)
                            )
                        )
                    )
                )
            )
        );

        return true;
    }

    @Override
    public boolean lobbyStart() {
        if (this.players.size() >= this.minPlayers) {
            Bedwars bw = new Bedwars(this.players, mapName);
            WarpManager.bedwars.add(bw);
            WarpManager.registerWarp(bw);
            bw.startActivity();
            lobbyDelete();
            return true;
        }
        return false;
    }

    @Override
    public void lobbyDelete() {
        players.forEach(p -> {
            PlayerInfos infos = PlayersInfos.get(p);
            infos.setLobby(null);
        });
        players.clear();
        LobbyManager.unregisterLobby(this);
    }

    @Override
    public void broadcastMessage(String message) {
        players.forEach(p -> {
            p.sendMessage(message);
        });
    }

    @Override
    public void broadcastMessage(Component component) {
        players.forEach(p -> {
            p.sendMessage(component);
        });
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}
