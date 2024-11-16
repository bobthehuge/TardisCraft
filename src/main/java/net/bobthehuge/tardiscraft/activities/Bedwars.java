package net.bobthehuge.tardiscraft.activities;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import net.bobthehuge.tardiscraft.Tardiscraft;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Bedwars implements Warp {

    private final WarpType warpType = WarpType.BEDWARS;
    private final String worldName;
    private final String name;

    private WarpState warpState;
    private List<Player> players = new ArrayList<Player>();

    public Bedwars(String worldName) {
        this.warpState = WarpState.INIT;
        this.worldName = worldName;
        this.name = "bedwars_" + Tardiscraft.BedwarsInstances.size();
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
    public void CreateWarp() {
        if (!Tardiscraft.MVC.getMVWorldManager().isMVWorld(worldName)) {
            try {
                throw new RuntimeException(String.format("Can't find %s world", worldName));
            } catch (Exception e) {
                Bukkit.getLogger().warning(e.getMessage());
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

    @Override
    public void PlayerJoin(Player player) {

    }

    @Override
    public void PlayerLeave(Player player) {

    }

    @Override
    public void StartActivity() {

    }

    @Override
    public void StopActivity() {

    }
}
