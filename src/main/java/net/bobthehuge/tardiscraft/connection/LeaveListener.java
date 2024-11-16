package net.bobthehuge.tardiscraft.connection;

import net.bobthehuge.tardiscraft.Tardiscraft;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LeaveListener implements Listener {
    @EventHandler
    public void onDisconnect(PlayerJoinEvent event) {
        Tardiscraft.Hub.PlayerLeave(event.getPlayer());
    }
}
