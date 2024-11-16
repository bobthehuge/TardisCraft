package net.bobthehuge.tardiscraft.lobbies;

import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.activities.Bedwars;
import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.activities.WarpState;
import net.bobthehuge.tardiscraft.activities.WarpType;
import org.bukkit.block.Bed;
import org.bukkit.entity.Player;

import java.util.List;

public class BedwarsLobby implements Lobby {

    private final WarpType warpType = WarpType.BEDWARS;
    private final String mapName;
    private List<Player> players = List.of();
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
    public void PlayerJoin(Player player) {
        this.players.add(player);
        Tardiscraft.PlayersInfos.get(player).setLobby(this);
    }

    @Override
    public void PlayerLeave(Player player) {
        this.players.remove(player);
        Tardiscraft.PlayersInfos.get(player).setLobby(null);
    }

    @Override
    public void LobbyStart() {
        if (this.players.size() >= this.minPlayers) {
            Bedwars bw = new Bedwars(mapName);

            this.players.forEach(player -> {
               this.PlayerLeave(player);
               bw.PlayerJoin(player);
            });

            Tardiscraft.BedwarsInstances.add(bw);
            bw.StartActivity();
        }
        LobbyEnd();
    }

    @Override
    public void LobbyEnd() {
    }
}
