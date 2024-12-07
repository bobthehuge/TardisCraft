package net.bobthehuge.tardiscraft.generators;

import org.bukkit.Location;
import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class SkyWarsGenerator implements Generator {
    private static final Random RANDOM = new Random();

    private final BedWarsGenerator<SkyBlockIslandGenerator> bedwarsGen = new BedWarsGenerator<>(new SkyBlockIslandGenerator());
    private final Generator middleIslandGen = new SphericalIslandGenerator(13, 5);

    private final int radius;
    private final int islands;

    private final List<Map<Bed.Part, Location>> bedLocations;

    public SkyWarsGenerator(int radius, int islands) {
        this.radius = radius;
        this.islands = islands;

        bedLocations = new ArrayList<>(islands);
    }

    @Override
    public void generate(@NotNull Context ctx) {
        generateBedWarsIslands(ctx);
        middleIslandGen.generate(ctx);
    }

    private void generateBedWarsIslands(@NotNull Context ctx) {
        for (int i = 0; i < islands; i++) {
            double angle = 2 * Math.PI * i / islands;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Location loc = ctx.fromOrigin((int) x, 0, (int) z);

            generateBedWarsIsland(Generator.makeContext(ctx.world(), loc, RANDOM));
        }
    }

    private void generateBedWarsIsland(@NotNull Context ctx) {
        bedwarsGen.generate(ctx);
        bedLocations.add(bedwarsGen.getBedLocation(ctx));
    }

    public Stream<Location> streamBedLocations(@NotNull Bed.Part part) {
        return bedLocations.stream()
                .map(map -> map.get(part).clone());
    }
}
