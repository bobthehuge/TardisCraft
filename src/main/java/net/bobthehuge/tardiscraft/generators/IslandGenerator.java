package net.bobthehuge.tardiscraft.generators;

import org.jetbrains.annotations.NotNull;

public interface IslandGenerator extends Generator {
    void generateIsland(@NotNull Context ctx);
    void decorateIsland(@NotNull Context ctx);

    @Override
    default void generate(@NotNull Context ctx) {
        generateIsland(ctx);
        decorateIsland(ctx);
    }
}
