package net.bobthehuge.tardiscraft.generators.decorators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GrassDecorator implements Decorator {
    private static final Random RANDOM = new Random();

    private final float proportion;
    private final Set<@NotNull Location> decorations = new HashSet<>();

    public GrassDecorator(float proportion) {
        this.proportion = proportion;
    }

    @Override
    public void decorate() {
        for (Location location : decorations) {
            if (RANDOM.nextFloat() < proportion) {
                decorateAt(location);
            }
        }
    }

    @Override
    public void addDecoration(@NotNull Location location) {
        decorations.add(location.clone());
    }

    private void decorateAt(@NotNull Location location) {
        Block block = location.getBlock();
        if (block.getType() != Material.GRASS_BLOCK) {
            return;
        }

        Location decrationLocation = location.clone().add(0, 1, 0);
        Block decoration = decrationLocation.getBlock();
        if (decoration.getType() != Material.AIR) {
            return;
        }

        decoration.setType(Material.GRASS);
    }
}
