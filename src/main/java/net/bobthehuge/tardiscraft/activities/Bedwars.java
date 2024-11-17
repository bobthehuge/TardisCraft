package net.bobthehuge.tardiscraft.activities;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.managers.WarpManager;
import net.bobthehuge.tardiscraft.playerinfos.PlayerInfos;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.List;

import static net.bobthehuge.tardiscraft.Tardiscraft.MVC;
import static net.bobthehuge.tardiscraft.Tardiscraft.PlayersInfos;
import static net.bobthehuge.tardiscraft.managers.WarpManager.warpPlayer;

public class Bedwars implements Warp {

    private final WarpType warpType = WarpType.BEDWARS;
    private final String worldName;
    private final String name;

    private WarpState warpState;
    private List<Player> players;

    public Bedwars(List<Player> players, String worldName) {
        this.warpState = WarpState.INIT;
        this.worldName = worldName;
        this.name = "bedwars_" + WarpManager.bedwars.size();

        this.players = players;

        MVC.getMVWorldManager().loadWorld(worldName);

        players.forEach(p -> {
            warpPlayer(p, this);
        });
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

    @Override
    public WarpType getWarpType() {
        return this.warpType;
    }

    @Override
    public MultiverseWorld getWorld() {
        return Tardiscraft.MVC.getMVWorldManager().getMVWorld(getWorldName());
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
            try {
                throw new RuntimeException(String.format("Can't find %s world", worldName));
            } catch (Exception e) {
                Bukkit.getLogger().warning(e.getMessage());
                return;
            }
        }

        MultiverseWorld w = getWorld();
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
        cbw.setGameRule(GameRule.DO_FIRE_TICK, true);
        cbw.setGameRule(GameRule.DO_TILE_DROPS, true);
        cbw.setGameRule(GameRule.MOB_GRIEFING, true);

        cbw.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        cbw.setGameRule(GameRule.DO_INSOMNIA, false);
        cbw.setGameRule(GameRule.DO_MOB_LOOT, false);
        cbw.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        cbw.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
        cbw.setGameRule(GameRule.DO_VINES_SPREAD, false);
        cbw.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
    }

    @Override
    public void playerJoin(Player player) {
        if (players.contains(player)) {
            return;
        }
        PlayersInfos.get(player).setWarp(this);
        players.add(player);
    }

    @Override
    public void playerLeave(Player player) {
        if (!players.contains(player)) {
            return;
        }
        WarpManager.hub.playerJoin(player);
        players.remove(player);
    }

    @Override
    public void startActivity() {

    }

    @Override
    public void stopActivity() {
        players.forEach(p -> {
            warpPlayer(p, WarpManager.hub);
        });
    }
}
