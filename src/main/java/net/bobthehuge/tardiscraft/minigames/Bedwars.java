package net.bobthehuge.tardiscraft.minigames;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import net.bobthehuge.tardiscraft.Tardiscraft;
import net.bobthehuge.tardiscraft.Tuple;
import net.bobthehuge.tardiscraft.managers.MinigameManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Bedwars implements Minigame {
    public static final String[] mapNames = {
        "sandcastle"
    };

    private final String ID;
    private final MinigameType minigameType = MinigameType.BEDWARS;
    private final MultiverseWorld world;
    private final List<Player> players = new ArrayList<>();
    private final int minPlayers;
    private final int maxPlayers;

    private MinigameState minigameState = MinigameState.LOBBY;

    private enum PlayerGameState {
        WAITING,
        PLAYING,
        WINNING,
        LOSING,
        SPECTATING,
    }

    private static class PlayerGameInfos {
        public PlayerGameState state;
    }

    private final HashMap<UUID, PlayerGameInfos> gameInfos = new HashMap<>();

    public Bedwars(String mapName, int minPlayers, int maxPlayers) {
        this.ID = minigameType.toString() + "_" + mapName + "_" + MinigameManager.getInstancesCount(MinigameType.BEDWARS);

        if (!Tardiscraft.MVC.cloneWorld(mapName, this.ID, ""))
            throw new RuntimeException(this.ID + ": Could not clone base world " + mapName);

        this.world = Tardiscraft.MVC.getMVWorldManager().getMVWorld(this.ID);
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public Tuple<Boolean, String> gameStart() {
        if (minigameState != MinigameState.LOBBY)
            return new Tuple<>(false, ID + ": Game has already been started");
        nextMinigameState();

        return new Tuple<>(true, ID + ": Game has been started");
    }

    @Override
    public Tuple<Boolean, String> gameEnd() {
        if (minigameState != MinigameState.PLAYING)
            return new Tuple<>(false, ID + ": Game has already been ended");
        nextMinigameState();

        return new Tuple<>(true, ID + ": Game has been ended");
    }

    @Override
    public Tuple<Boolean, String> gameDelete() {
        if (minigameState != MinigameState.ENDING)
            return new Tuple<>(false, ID + ": Game has already been deleted");
        nextMinigameState();

        return new Tuple<>(true, ID + ": Game has been deleted");
    }

    @Override
    public Tuple<Boolean, String> playerJoin(Player player) {
        if (players.contains(player))
            return new Tuple<>(false, ID + ": Player " + player.getName() + " already in game");

        if (minigameState == MinigameState.DESTROYING) {
            return new Tuple<>(false, ID + ": Game has already ended");
        }

        players.add(player);
        PlayerGameInfos infos = new PlayerGameInfos();

        switch (minigameState) {
            case LOBBY: {
                if (players.size() == maxPlayers)
                    infos.state = PlayerGameState.SPECTATING;
                else
                    infos.state = PlayerGameState.WAITING;
            }
            case PLAYING: case ENDING: {
                infos.state = PlayerGameState.SPECTATING;
            }
        }

        gameInfos.put(player.getUniqueId(), infos);
        return new Tuple<>(true, ID + ": Player " + player.getName() + " joined");
    }

    @Override
    public Tuple<Boolean, String> playerLeave(Player player) {
        if (!players.contains(player))
            return new Tuple<>(false, ID + ": Player " + player.getName() + " not in game");

        players.remove(player);
        gameInfos.remove(player.getUniqueId());

        return new Tuple<>(true, ID + ": Player " + player.getName() + " left");
    }

    private void nextMinigameState() {
        gameInfos.forEach((id, infos) -> {
           switch (minigameState) {
               case LOBBY: {
                   if (infos.state == PlayerGameState.WAITING)
                       infos.state = PlayerGameState.PLAYING;

                   minigameState = MinigameState.PLAYING;
               }
               case PLAYING: {
                   if (infos.state == PlayerGameState.PLAYING)
                       infos.state = PlayerGameState.WINNING;

                   minigameState = MinigameState.ENDING;
               }
               case ENDING: {
                    minigameState = MinigameState.DESTROYING;
               }
           }
        });
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public MultiverseWorld getWorld() {
        return world;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public MinigameState getMinigameState() {
        return minigameState;
    }

    @Override
    public MinigameType getMinigameType() {
        return minigameType;
    }
}
