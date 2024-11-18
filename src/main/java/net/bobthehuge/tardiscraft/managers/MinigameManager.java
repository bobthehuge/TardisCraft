package net.bobthehuge.tardiscraft.managers;

import net.bobthehuge.tardiscraft.minigames.MinigameType;
import net.bobthehuge.tardiscraft.minigames.Minigame;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class MinigameManager {
    private static final HashMap<MinigameType, LinkedHashSet<Minigame>> minigames = new HashMap<>() {
        {
            put(MinigameType.BEDWARS, new LinkedHashSet<Minigame>());
        }
    };

    public static boolean registerMinigame(MinigameType t, Minigame g) {
        boolean state = minigames.get(t).add(g);
        Bukkit.getLogger().info("Registered Minigame: " + t);
        return state;
    }

    public static boolean unregisterMinigame(MinigameType t, Minigame g) {
        boolean state = minigames.get(t).remove(g);
        Bukkit.getLogger().info("Unregistered Minigame: " + t);
        return state;
    }

    public static LinkedHashSet<Minigame> getInstancesOf(MinigameType t) {
        return minigames.get(t);
    }

    public static int getInstancesCount(MinigameType t) {
        return minigames.get(t).size();
    }
}
