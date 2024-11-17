package net.bobthehuge.tardiscraft.listeners;

import net.bobthehuge.tardiscraft.activities.Warp;
import net.bobthehuge.tardiscraft.managers.WarpManager;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.bobthehuge.tardiscraft.Tardiscraft.PlayersInfos;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInfos infos = PlayersInfos.get(player);

        if (infos == null) {
            PlayersInfos.put(player, new PlayerInfos(player));
        }
        else {
            Warp old = infos.getWarp();
            if (old != null) {
                old.playerJoin(player);
            }
        }

        //WarpManager.hub.playerJoin(event.getPlayer());
        WarpManager.warpPlayer(player, WarpManager.hub);
    }
}
