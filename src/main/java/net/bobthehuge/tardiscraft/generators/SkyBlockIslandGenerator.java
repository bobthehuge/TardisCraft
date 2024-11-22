package net.bobthehuge.tardiscraft.generators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class SkyBlockIslandGenerator implements IslandGenerator {
    @Override
    public void generateIsland(@NotNull Context ctx) {
        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < 6 && (i < 3 || k < 3); k++) {
                Location loc = ctx.fromOrigin(i - 1, -3, k - 1);
                loc.getBlock().setType(Material.DIRT);
                loc.add(0, 1, 0).getBlock().setType(Material.DIRT);
                loc.add(0, 1, 0).getBlock().setType(Material.GRASS_BLOCK);
            }
        }
    }

    @Override
    public void decorateIsland(@NotNull Context ctx) {
        generateTree(ctx);
        generateChest(ctx);
    }

    private void generateTree(@NotNull Context ctx) {
        Location loc = ctx.fromOrigin(3, 0, 0);
        ctx.world().generateTree(loc, TreeType.TREE);
    }

    private void generateChest(@NotNull Context ctx) {
        Location loc = ctx.fromOrigin(0, 0, 3);
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
}
