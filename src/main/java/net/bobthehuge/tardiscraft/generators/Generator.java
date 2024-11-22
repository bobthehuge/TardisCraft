package net.bobthehuge.tardiscraft.generators;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface Generator {
    void generate(@NotNull Context ctx);

    static Context makeContext(@NotNull World world, @NotNull Location origin, @NotNull Random random) {
        int px = random.nextInt(0, 2) * 2 - 1;
        int pz = random.nextInt(0, 2) * 2 - 1;
        boolean xz = random.nextBoolean();

        return new Context(world, px, pz, xz, origin);
    }

    record Context(@NotNull World world, int px, int pz, boolean xz, @NotNull Location origin) {
        public Context(@NotNull World world, int px, int pz, boolean xz, @NotNull Location origin) {
            this.world = world;
            this.px = px;
            this.pz = pz;
            this.xz = xz;
            this.origin = origin.clone();
        }

        public @NotNull Location fromOrigin(int x, int y, int z) {
            int i = x * px;
            int k = z * pz;
            return xz ? origin.clone().add(i, y, k) : origin.clone().add(k, y, i);
        }

        public @NotNull BlockFace getNorth() {
            return xz ? (pz > 0 ? BlockFace.NORTH : BlockFace.SOUTH) : (pz > 0 ? BlockFace.WEST : BlockFace.EAST);
        }

        public @NotNull BlockFace getSouth() {
            return xz ? (pz > 0 ? BlockFace.SOUTH : BlockFace.NORTH) : (pz > 0 ? BlockFace.EAST : BlockFace.WEST);
        }

        public @NotNull BlockFace getEast() {
            return xz ? (px > 0 ? BlockFace.EAST : BlockFace.WEST) : (px > 0 ? BlockFace.SOUTH : BlockFace.NORTH);
        }

        public @NotNull BlockFace getWest() {
            return xz ? (px > 0 ? BlockFace.WEST : BlockFace.EAST) : (px > 0 ? BlockFace.NORTH : BlockFace.SOUTH);
        }
    }
}
