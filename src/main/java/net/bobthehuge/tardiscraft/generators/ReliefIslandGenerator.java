package net.bobthehuge.tardiscraft.generators;

import net.bobthehuge.tardiscraft.generators.decorators.Decorator;
import net.bobthehuge.tardiscraft.generators.decorators.GrassDecorator;
import net.bobthehuge.tardiscraft.generators.decorators.TreeDecorator;
import net.bobthehuge.tardiscraft.utils.Gradient;
import net.bobthehuge.tardiscraft.utils.PerlinNoise;
import net.bobthehuge.tardiscraft.utils.Smooth;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ReliefIslandGenerator implements IslandGenerator {
    private final Decorator grassDecorator = new GrassDecorator(0.7f);
    private final Decorator treeDecorator = new TreeDecorator(1.0f, 0.0f);

    private final int radius;
    private final int seed;
    private final double frequency;
    private final int size;

    public ReliefIslandGenerator(int radius, int seed, double frequency) {
        this.radius = radius;
        this.seed = seed;
        this.frequency = frequency;

        size = 2 * radius + 1;
    }

    @Override
    public void generateIsland(@NotNull Context ctx) {
        double[][] noise = calculateNoise();
        double[][] gradient = Gradient.euclidean(size);

        assert noise.length == size && noise[0].length == size;
        assert gradient.length == size && gradient[0].length == size;

        double[][] profile = calculateProfile(gradient);
        int[][] groundHeightMap = calculateGroundHeightMap(noise, profile);
        int[][] groundLevel = calculateGroundLevel(groundHeightMap);
        int[][] undergroundHeightMap = calculateUndergroundHeightMap(groundLevel, groundHeightMap);

        generateStoneLayer(ctx, undergroundHeightMap, groundLevel);
        generateDirtLayer(ctx, groundHeightMap, groundLevel);
    }

    @Override
    public void decorateIsland(@NotNull Context ctx) {
        treeDecorator.decorate();
        grassDecorator.decorate();
    }

    private void generateDirtLayer(@NotNull Context ctx, int[][] heightMap, int[][] groundLevel) {
        for (int x = 1; x < size - 1; x++) {
            for (int z = 1; z < size - 1; z++) {
                int g = groundLevel[x][z];
                if (g < 0) {
                    continue;
                }

                int y = Math.max(0, heightMap[x][z]);
                Location loc = ctx.fromOrigin(x - radius, y, z - radius);
                loc.getBlock().setType(Material.GRASS_BLOCK);
                grassDecorator.addDecoration(loc.clone());

                for (; y > g; y--) {
                    loc.subtract(0, 1, 0).getBlock().setType(Material.DIRT);
                }
            }
        }
    }

    private void generateStoneLayer(@NotNull Context ctx, int[][] heightMap, int[][] groundLevel) {
        for (int x = 1; x < size - 1; x++) {
            for (int z = 1; z < size - 1; z++) {
                int g = groundLevel[x][z];
                int y = heightMap[x][z];

                Location loc = ctx.fromOrigin(x - radius, y, z - radius);
                for (; y < g; y++) {
                    loc.getBlock().setType(Material.STONE);
                    loc.add(0, 1, 0);
                }
            }
        }
    }

    private double[][] calculateNoise() {
        double[][] noise = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = i - radius;
                int z = j - radius;
                noise[i][j] = PerlinNoise.noise((x * frequency) + seed, (z * frequency) + seed)
                        + 0.50 * PerlinNoise.noise((x * 2 * frequency) + seed, (z * 2 * frequency) + seed)
                        + 0.25 * PerlinNoise.noise((x * 4 * frequency) + seed, (z * 4 * frequency) + seed)
                        + 0.13 * PerlinNoise.noise((x * 8 * frequency) + seed, (z * 8 * frequency) + seed)
                        + 0.06 * PerlinNoise.noise((x * 16 * frequency) + seed, (z * 16 * frequency) + seed)
                        + 0.03 * PerlinNoise.noise((x * 32 * frequency) + seed, (z * 32 * frequency) + seed);
            }
        }
        return noise;
    }

    private double[][] calculateProfile(double[][] gradient) {
        double[][] profile = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                profile[i][j] = (Math.pow(gradient[i][j], 0.82) * 2 - radius) * 0.24;
            }
        }
        return profile;
    }

    private int[][] calculateGroundHeightMap(double[][] noise, double[][] profile) {
        double[][] groundProfile = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double e = (noise[i][j] + 1) * 0.5;
                double h = 16 * (Math.pow(e, 0.5) - 1);
                groundProfile[i][j] = (int) (h - profile[i][j]);
            }
        }
        double[][] smoothProfile = Smooth.smooth(groundProfile, 12);
        int[][] heightMap = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                heightMap[i][j] = (int) smoothProfile[i][j];
            }
        }
        return heightMap;
    }

    private int[][] calculateGroundLevel(int[][] heightMap) {
        int[][] groundLevel = new int[size][size];
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                if (x == 0 || x == size - 1 || z == 0 || z == size - 1) {
                    groundLevel[x][z] = -1;
                    continue;
                }

                if (heightMap[x - 1][z] <= 0 && heightMap[x][z - 1] <= 0 && heightMap[x + 1][z] <= 0 && heightMap[x][z + 1] <= 0) {
                    groundLevel[x][z] = -1;
                    continue;
                }

                groundLevel[x][z] = Math.max(0, heightMap[x][z] - 3);
            }
        }
        return groundLevel;
    }

    private int[][] calculateUndergroundHeightMap(int[][] groundLevel, int[][] groundHeightMap) {
        double[][] profile = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (groundLevel[i][j] < 0) {
                    continue;
                }

                if (groundHeightMap[i][j] == 0) {
                    profile[i][j] = -1;
                    continue;
                }

                profile[i][j] = -Math.pow(groundHeightMap[i][j], 0.8) * 4.5;
            }
        }

        double[][] smoothProfile = Smooth.smooth(profile, 5);
        int[][] heightMap = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                heightMap[i][j] = (int) smoothProfile[i][j];
            }
        }
        return heightMap;
    }
}
