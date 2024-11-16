package net.bobthehuge.tardiscraft.connection;

import net.bobthehuge.tardiscraft.Tardiscraft;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Tardiscraft.Hub.PlayerJoin(event.getPlayer());
    }
}
