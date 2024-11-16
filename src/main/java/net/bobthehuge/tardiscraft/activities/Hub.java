package net.bobthehuge.tardiscraft.activities;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.api.Teleporter;
import com.onarandombox.MultiverseCore.utils.SimpleSafeTTeleporter;
import net.bobthehuge.tardiscraft.Tardiscraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.TriState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hub implements Warp
{
    public List<Player> players = new ArrayList<Player>();

    public final String world_name = "creative";

    public final MultiverseWorld world = Tardiscraft.MVC
        .getMVWorldManager()
        .getMVWorld(world_name);

    private enum PlayerStates {
        WANDERING,
    }

    public void PlayerJoin(Player player) {
        Tardiscraft.MVC.getMVWorldManager().loadWorld(world_name);
        Tardiscraft.PlayerLocations.put(player, this);

        SetPlayerState(player, PlayerStates.WANDERING);
        Tardiscraft.MVC.teleportPlayer(
            (CommandSender) Tardiscraft.MVC.getSafeTTeleporter(),
            player,
            world.getSpawnLocation()
        );

        players.add(player);

        world.getCBWorld().sendMessage(Component.text(
            String.format("%s has joined !", player.getName()),
            NamedTextColor.GREEN
        ));
    }

    public void PlayerLeave(Player player) {
        players.remove(player);

        if (players.isEmpty())
            Tardiscraft.MVC.getMVWorldManager().unloadWorld(world_name);
    }

    private void SetPlayerState(Player player, PlayerStates state) {
        switch (state) {
            case WANDERING -> {
                player.setFlyingFallDamage(TriState.FALSE);
                player.setAllowFlight(true);
            }
        }

        if (player.isOp())
            player.sendMessage(Component.text(
                "Switched mode to " + state.toString(),
                NamedTextColor.GRAY
            ).decorate(TextDecoration.ITALIC));
    }
}
