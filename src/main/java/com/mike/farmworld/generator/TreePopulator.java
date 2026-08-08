package com.mike.farmworld.generator;

import com.mike.farmworld.FarmWorldPlugin;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TreePopulator extends BlockPopulator {

    private final int biomeZoneSize;

    public TreePopulator(int biomeZoneSize) {
        this.biomeZoneSize = biomeZoneSize;
    }

    @Override
    public void populate(@NotNull org.bukkit.generator.WorldInfo worldInfo, @NotNull Random random,
                          int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {

        double chance = FarmWorldPlugin.getInstance().getConfig().getDouble("tree-chance-per-chunk", 0.35);
        if (random.nextDouble() > chance) return;

        int worldX = (chunkX << 4) + random.nextInt(16);
        int worldZ = (chunkZ << 4) + random.nextInt(16);

        FarmBiome biome = FarmBiome.forColumn(worldX, worldZ, biomeZoneSize);
        int surfaceY = 64; // entspricht SURFACE_Y aus dem Generator

        if (!limitedRegion.isInRegion(worldX, surfaceY, worldZ)) return;

        Material ground = limitedRegion.getType(worldX, surfaceY, worldZ);
        if (ground != biome.getSurfaceBlock()) return;

        switch (biome) {
            case PLAINS -> buildGoldenTree(limitedRegion, worldX, surfaceY + 1, worldZ, random);
            case FOREST -> buildRedwood(limitedRegion, worldX, surfaceY + 1, worldZ, random);
            case DESERT -> buildPalm(limitedRegion, worldX, surfaceY + 1, worldZ, random);
            case TAIGA -> buildFrostTree(limitedRegion, worldX, surfaceY + 1, worldZ, random);
            case SWAMP -> buildMangrove(limitedRegion, worldX, surfaceY + 1, worldZ, random);
            case SAVANNA -> buildAcaciaSpread(limitedRegion, worldX, surfaceY + 1, worldZ, random);
        }
    }

    // ---- Baumformen ----

    private void buildGoldenTree(LimitedRegion region, int x, int y, int z, Random random) {
        int height = 5 + random.nextInt(2);
        for (int i = 0; i < height; i++) {
            set(region, x, y + i, z, Material.OAK_LOG);
        }
        // Blätterkrone mit "goldenen" Spitzen
        canopy(region, x, y + height - 1, z, Material.OAK_LEAVES, 2);
        set(region, x, y + height + 1, z, Material.GLOWSTONE);
    }

    private void buildRedwood(LimitedRegion region, int x, int y, int z, Random random) {
        int height = 8 + random.nextInt(4);
        for (int i = 0; i < height; i++) {
            set(region, x, y + i, z, Material.SPRUCE_LOG);
        }
        canopy(region, x, y + height - 2, z, Material.SPRUCE_LEAVES, 3);
    }

    private void buildPalm(LimitedRegion region, int x, int y, int z, Random random) {
        int height = 4 + random.nextInt(2);
        int leanX = random.nextBoolean() ? 1 : 0;
        int curX = x;
        for (int i = 0; i < height; i++) {
            if (i > height / 2) curX = x + leanX;
            set(region, curX, y + i, z, Material.STRIPPED_JUNGLE_LOG);
        }
        canopy(region, curX, y + height, z, Material.JUNGLE_LEAVES, 2);
    }

    private void buildFrostTree(LimitedRegion region, int x, int y, int z, Random random) {
        int height = 6 + random.nextInt(3);
        for (int i = 0; i < height; i++) {
            set(region, x, y + i, z, Material.SPRUCE_LOG);
        }
        canopy(region, x, y + height - 2, z, Material.SPRUCE_LEAVES, 2);
        // Schneehaube
        set(region, x, y + height, z, Material.SNOW_BLOCK);
    }

    private void buildMangrove(LimitedRegion region, int x, int y, int z, Random random) {
        int height = 4 + random.nextInt(2);
        for (int i = 0; i < height; i++) {
            set(region, x, y + i, z, Material.MANGROVE_LOG);
        }
        canopy(region, x, y + height - 1, z, Material.MANGROVE_LEAVES, 2);
    }

    private void buildAcaciaSpread(LimitedRegion region, int x, int y, int z, Random random) {
        int height = 5 + random.nextInt(2);
        for (int i = 0; i < height; i++) {
            set(region, x, y + i, z, Material.ACACIA_LOG);
        }
        // breite, flache Krone
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (Math.abs(dx) + Math.abs(dz) <= 4) {
                    set(region, x + dx, y + height, z + dz, Material.ACACIA_LEAVES);
                }
            }
        }
    }

    private void canopy(LimitedRegion region, int cx, int cy, int cz, Material leaves, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -1; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= radius * radius + 1) {
                        set(region, cx + dx, cy + dy, cz + dz, leaves);
                    }
                }
            }
        }
    }

    private void set(LimitedRegion region, int x, int y, int z, Material material) {
        if (region.isInRegion(x, y, z)) {
            Material current = region.getType(x, y, z);
            if (current == Material.AIR || current.name().endsWith("LEAVES")) {
                region.setType(x, y, z, material);
            }
        }
    }
}
