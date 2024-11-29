package net.bobthehuge.tardiscraft.generators.decorators;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Decorator {
    void decorate();
    void addDecoration(@NotNull Location location);
}
