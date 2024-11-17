package net.bobthehuge.tardiscraft.listeners;

import net.bobthehuge.tardiscraft.lobbies.Lobby;
import net.bobthehuge.tardiscraft.managers.WarpManager;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.bobthehuge.tardiscraft.Tardiscraft.PlayersInfos;

public class LeaveListener implements Listener {
    @EventHandler
    public void onDisconnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInfos infos = PlayersInfos.get(player);

        infos.getWarp().playerLeave(player);
        Lobby old = infos.getLobby();

        if (old != null)
            old.playerLeave(player);
    }
}
