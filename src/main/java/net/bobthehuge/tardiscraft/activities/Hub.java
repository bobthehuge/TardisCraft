package net.bobthehuge.tardiscraft.activities;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MVDestination;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.destination.ExactDestination;
import com.onarandombox.MultiverseCore.destination.WorldDestination;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import net.bobthehuge.tardiscraft.Tardiscraft;
import net.kyori.adventure.Adventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hub implements Warp {
    private List<Player> players = new ArrayList<Player>();
    private final WarpState warpState = WarpState.INGAME;
    private final String worldName = "hub";

    private enum PlayerStates {
        WANDERING,
    }

    @Override
    public String getName() {
        return "hub";
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

    @Override
    public WarpType getWarpType() {
        return WarpType.HUB;
    }

    @Override
    public MultiverseWorld getWorld() {
        return Tardiscraft.MVC.getMVWorldManager().getMVWorld(worldName);
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public WarpState getWarpState() {
        return this.warpState;
    }

    @Override
    public void CreateWarp() {
        if (!Tardiscraft.MVC.getMVWorldManager().isMVWorld(worldName)) {
            Tardiscraft.MVC.getMVWorldManager().addWorld(
                worldName,
                World.Environment.NORMAL,
                null,
                WorldType.FLAT,
                false,
                null
            );
        }

        MultiverseWorld w = Tardiscraft.MVC.getMVWorldManager().getMVWorld(worldName);
        World cbw = w.getCBWorld();

        w.setAllowAnimalSpawn(false);
        w.setDifficulty(Difficulty.PEACEFUL);
        w.setAutoLoad(false);
        w.setGameMode(GameMode.ADVENTURE);
        w.setAllowFlight(true);
        w.setHunger(false);
        w.setPVPMode(false);
        w.setAllowMonsterSpawn(false);
        w.setEnableWeather(false);
        w.setTime("noon");

        cbw.setGameRule(GameRule.DISABLE_RAIDS, true);
        cbw.setGameRule(GameRule.KEEP_INVENTORY, true);
        cbw.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        cbw.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        cbw.setGameRule(GameRule.DO_FIRE_TICK, false);
        cbw.setGameRule(GameRule.DO_INSOMNIA, false);
        cbw.setGameRule(GameRule.DO_MOB_LOOT, false);
        cbw.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_VINES_SPREAD, false);
        cbw.setGameRule(GameRule.DO_TILE_DROPS, false);
        cbw.setGameRule(GameRule.MOB_GRIEFING, false);
        cbw.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
    }

    public void PlayerJoin(Player player) {
        Tardiscraft.MVC.getMVWorldManager().loadWorld(worldName);

        PlayerInfos infos = new PlayerInfos(player);
        infos.setWarp(this);
        Tardiscraft.PlayersInfos.put(player,infos);

        SetPlayerState(player, PlayerStates.WANDERING);

        ExactDestination dst = new ExactDestination();
        dst.setDestination(getWorld().getSpawnLocation());

        Tardiscraft.MVC.getSafeTTeleporter().safelyTeleport(
            Bukkit.getConsoleSender(),
            player,
            dst
        );

        players.add(player);

        getWorld().getCBWorld().sendMessage(Component.text(
            String.format("%s has joined !", player.getName()),
            NamedTextColor.GREEN
        ));
    }

    public void PlayerLeave(Player player) {
        players.remove(player);

        if (players.isEmpty())
            Tardiscraft.MVC.getMVWorldManager().unloadWorld(worldName);
    }

    @Override
    public void StartActivity() {

    }

    @Override
    public void StopActivity() {

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
