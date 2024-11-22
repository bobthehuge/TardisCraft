package net.bobthehuge.tardiscraft.generators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BedWarsGenerator<ISLAND extends IslandGenerator> implements Generator {
    private final @NotNull ISLAND island;

    public BedWarsGenerator(@NotNull ISLAND island) {
        this.island = island;
    }

    private void generateBed(@NotNull Context ctx) {
        Map<Bed.Part, Location> locs = getBedLocation(ctx);
        for (Bed.Part part : Bed.Part.values()) {
            Location loc = locs.get(part);

            Block block = loc.getBlock();
            block.setType(Material.WHITE_BED);

            Block bed = loc.getBlock();
            if (bed.getBlockData() instanceof Bed data) {
                data.setFacing(getBedFacing(ctx));
                data.setPart(part);
                bed.setBlockData(data);
            }
        }
    }

    @Override
    public void generate(@NotNull Context ctx) {
        island.generate(ctx);
        generateBed(ctx);
    }

    public Map<Bed.Part, Location> getBedLocation(@NotNull Context ctx) {
        return Map.of(
                Bed.Part.FOOT, ctx.fromOrigin(1, 0, 0),
                Bed.Part.HEAD, ctx.fromOrigin(2, 0, 0)
        );
    }

    public BlockFace getBedFacing(@NotNull Context ctx) {
        return ctx.getEast();
    }
}
