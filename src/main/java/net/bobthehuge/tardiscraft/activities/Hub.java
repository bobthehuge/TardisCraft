package net.bobthehuge.tardiscraft.activities;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.destination.ExactDestination;
import net.bobthehuge.tardiscraft.Tardiscraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hub implements Warp {
    private List<Player> players = new ArrayList<Player>();
    private final WarpState warpState = WarpState.INGAME;
    private final String worldName = "hub";

    private enum PlayerStates {
        WANDERING;
        public static PlayerStates getPlayerState(String state) {
            switch (state) {
                case "WANDERING": return WANDERING;
                default: return null;
            }
        }
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
    public void createWarp() {
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
        w.setEnableWeather(false);

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

    public void playerJoin(Player player) {
        Tardiscraft.MVC.getMVWorldManager().loadWorld(worldName);
        Tardiscraft.PlayersInfos.get(player).setWarp(this);

        setPlayerState(player, PlayerStates.WANDERING);

        players.add(player);

        getWorld().getCBWorld().sendMessage(Component.text(
            String.format("%s has joined !", player.getName()),
            NamedTextColor.GREEN
        ));
    }

    public void playerLeave(Player player) {
        players.remove(player);

        if (players.isEmpty())
            Tardiscraft.MVC.getMVWorldManager().unloadWorld(worldName);
    }

    @Override
    public void startActivity() {

    }

    @Override
    public void stopActivity() {

    }

    private void setPlayerState(Player player, PlayerStates state) {
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
