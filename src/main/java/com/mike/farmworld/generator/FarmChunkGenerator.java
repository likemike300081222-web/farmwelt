package com.mike.farmworld.generator;

import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class FarmChunkGenerator extends ChunkGenerator {

    private static final int STONE_TOP_Y = 60;
    private static final int SUBSURFACE_TOP_Y = 63;
    private static final int SURFACE_Y = 64;

    private final int biomeZoneSize;

    public FarmChunkGenerator(int biomeZoneSize) {
        this.biomeZoneSize = biomeZoneSize;
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random,
                                 int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        int minY = worldInfo.getMinHeight();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, minY, z, Material.BEDROCK);

                for (int y = minY + 1; y <= STONE_TOP_Y; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }

                int worldX = (chunkX << 4) + x;
                int worldZ = (chunkZ << 4) + z;
                FarmBiome biome = FarmBiome.forColumn(worldX, worldZ, biomeZoneSize);

                for (int y = STONE_TOP_Y + 1; y <= SUBSURFACE_TOP_Y; y++) {
                    chunkData.setBlock(x, y, z, biome.getSubSurfaceBlock());
                }

                chunkData.setBlock(x, SURFACE_Y, z, biome.getSurfaceBlock());
            }
        }
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull org.bukkit.World world) {
        return List.of(
                new TreePopulator(biomeZoneSize),
                new DungeonPopulator()
        );
    }
}
