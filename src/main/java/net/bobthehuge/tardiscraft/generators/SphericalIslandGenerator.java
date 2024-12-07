package net.bobthehuge.tardiscraft.generators;

import net.bobthehuge.tardiscraft.generators.decorators.Decorator;
import net.bobthehuge.tardiscraft.generators.decorators.GrassDecorator;
import net.bobthehuge.tardiscraft.generators.decorators.TreeDecorator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class SphericalIslandGenerator implements IslandGenerator {
    private final Decorator grassDecorator = new GrassDecorator(0.8f);
    private final Decorator treeDecorator = new TreeDecorator(0.05f, 0.3f);

    private final int radius;
    private final int height;

    public SphericalIslandGenerator(int radius, int height) {
        this.radius = radius;
        this.height = height;
    }

    @Override
    public void generateIsland(@NotNull Context ctx) {
        for (int y = 1; y <= height; y++) {
            generateLayer(ctx, y);
        }
    }

    @Override
    public void decorateIsland(@NotNull Context ctx) {
        treeDecorator.decorate();
        grassDecorator.decorate();
    }

    private void generateLayer(@NotNull Context ctx, int y) {
        int sphereY = y - radius;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= this.radius; z++) {
                if (x * x + sphereY * sphereY + z * z > this.radius * this.radius)
                    continue;

                Location loc = ctx.fromOrigin(x, y - height - 1, z);
                if (y == height) {
                    loc.getBlock().setType(Material.GRASS_BLOCK);
                    grassDecorator.addDecoration(loc.clone());
                    treeDecorator.addDecoration(loc.clone());
                } else {
                    loc.getBlock().setType(Material.DIRT);
                }
            }
        }
    }
}
