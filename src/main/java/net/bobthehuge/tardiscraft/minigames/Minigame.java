package net.bobthehuge.tardiscraft.minigames;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import net.bobthehuge.tardiscraft.Tuple;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface Minigame {
    public String getID();
    public MultiverseWorld getWorld();

    public List<Player> getPlayers();
    public MinigameState getMinigameState();
    public MinigameType getMinigameType();

    public Tuple<Boolean, String> gameStart();
    public Tuple<Boolean, String> gameEnd();
    public Tuple<Boolean, String> gameDelete();

    public Tuple<Boolean, String> playerJoin(Player player);
    public Tuple<Boolean, String> playerLeave(Player player);
}
