package net.bobthehuge.tardiscraft.playerinfos;

import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.lobbies.Lobby;
import org.bukkit.entity.Player;

public class PlayerInfos {
    private final Player player;
    private Warp warp = null;
    private Lobby lobby = null;
    private PlayerRole role = PlayerRole.STANDARD;

    public PlayerInfos(Player player) {
        this.player = player;

        if (player.isOp())
            role = PlayerRole.OP;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Warp getWarp() {
        return this.warp;
    }

    public void setWarp(Warp warp) {
        this.warp = warp;
    }

    public Lobby getLobby() {
        return this.lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public PlayerRole getRole() {
        return this.role;
    }
}
