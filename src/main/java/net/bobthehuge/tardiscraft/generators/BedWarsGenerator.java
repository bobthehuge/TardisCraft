package net.bobthehuge.tardiscraft.generators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Bed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BedWarsGenerator<ISLAND extends IslandGenerator> implements Generator {
    private final @NotNull ISLAND island;

    public BedWarsGenerator(@NotNull ISLAND island) {
        this.island = island;
    }

    @Override
    public void generate(@NotNull Context ctx) {
        island.generateIsland(ctx);
        generateBed(ctx);
        generateChest(ctx);
        island.decorateIsland(ctx);
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

    private void generateChest(@NotNull Context ctx) {
        Location loc = ctx.fromOrigin(0, 0, 4);
        loc.getBlock().setType(Material.CHEST);

        Chest chest = (Chest) loc.getBlock().getState();
        chest.getBlockInventory().addItem(
                new ItemStack(Material.ICE),
                new ItemStack(Material.LAVA_BUCKET),
                new ItemStack(Material.OAK_SAPLING),
                new ItemStack(Material.PUMPKIN_SEEDS),
                new ItemStack(Material.MELON_SEEDS),
                new ItemStack(Material.WHEAT_SEEDS),
                new ItemStack(Material.SUGAR_CANE),
                new ItemStack(Material.BROWN_MUSHROOM),
                new ItemStack(Material.RED_MUSHROOM)
        );

        Block block = loc.getBlock();
        if (block.getBlockData() instanceof Directional data) {
            data.setFacing(ctx.getNorth());
            block.setBlockData(data);
        }
    }

    public Map<Bed.Part, Location> getBedLocation(@NotNull Context ctx) {
        return Map.of(
                Bed.Part.FOOT, ctx.fromOrigin(0, 0, 2),
                Bed.Part.HEAD, ctx.fromOrigin(0, 0, 3)
        );
    }

    public BlockFace getBedFacing(@NotNull Context ctx) {
        return ctx.getSouth();
    }
}
