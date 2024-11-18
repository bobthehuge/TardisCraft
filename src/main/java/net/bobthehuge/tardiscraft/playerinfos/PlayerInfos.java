package net.bobthehuge.tardiscraft.playerinfos;

import net.bobthehuge.tardiscraft.minigames.Minigame;
import org.bukkit.entity.Player;

public class PlayerInfos {
    private final Player player;
    private Minigame game = null;
    private PlayerRole role = PlayerRole.STANDARD;

    public PlayerInfos(Player player) {
        this.player = player;

        if (player.isOp())
            role = PlayerRole.OP;
    }

    public Player getPlayer() {
        return this.player;
    }

    public PlayerRole getRole() {
        return this.role;
    }
}
