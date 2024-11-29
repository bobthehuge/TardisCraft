package net.bobthehuge.tardiscraft.generators;

import net.bobthehuge.tardiscraft.generators.decorators.Decorator;
import net.bobthehuge.tardiscraft.generators.decorators.GrassDecorator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class SkyBlockIslandGenerator implements IslandGenerator {
    private static final Random RANDOM = new Random();

    private final Decorator grassDecorator = new GrassDecorator(0.8f);

    @Override
    public void generateIsland(@NotNull Context ctx) {
        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < 6 && (i < 3 || k < 3); k++) {
                Location loc = ctx.fromOrigin(i - 1, -3, k - 1);
                loc.getBlock().setType(Material.DIRT);
                loc.add(0, 1, 0).getBlock().setType(Material.DIRT);
                loc.add(0, 1, 0).getBlock().setType(Material.GRASS_BLOCK);
                grassDecorator.addDecoration(loc.clone());
            }
        }
    }

    @Override
    public void decorateIsland(@NotNull Context ctx) {
        generateTree(ctx);
        grassDecorator.decorate();
    }

    private void generateTree(@NotNull Context ctx) {
        int xOffset = RANDOM.nextInt(5) / 4;
        int zOffset = (RANDOM.nextInt(10) - 5) / 4;
        Location loc = ctx.fromOrigin(3 + xOffset, 0, zOffset);
        ctx.world().generateTree(loc, TreeType.TREE);
    }
}
