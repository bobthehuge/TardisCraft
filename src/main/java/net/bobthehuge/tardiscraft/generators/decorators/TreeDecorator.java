package net.bobthehuge.tardiscraft.generators.decorators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TreeDecorator implements Decorator {
    private static final Random RANDOM = new Random();

    private final float proportion;
    private final float bigTreeProportion;

    private final Set<@NotNull Location> decorations = new HashSet<>();

    public TreeDecorator(float proportion, float bigTreeProportion) {
        this.proportion = proportion;
        this.bigTreeProportion = bigTreeProportion;
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
        decorations.add(location);
    }

    private void decorateAt(@NotNull Location location) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = location.clone().add(x, 0, z).getBlock();
                if (block.getType() != Material.GRASS_BLOCK) {
                    return;
                }
            }
        }

        for (int x = -2; x <= 2; x++) {
            for (int y = 1; y <= 3; y++) {
                for (int z = -2; z <= 2; z++) {
                    Block block = location.clone().add(x, y, z).getBlock();
                    if (block.getType() != Material.AIR) {
                        return;
                    }
                }
            }
        }

        Location decrationLocation = location.clone().add(0, 1, 0);
        World world = decrationLocation.getWorld();

        if (RANDOM.nextFloat() < bigTreeProportion) {
            world.generateTree(decrationLocation, TreeType.BIG_TREE);
        } else {
            world.generateTree(decrationLocation, TreeType.TREE);
        }
    }
}
