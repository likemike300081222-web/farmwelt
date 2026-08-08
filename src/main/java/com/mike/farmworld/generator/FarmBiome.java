package com.mike.farmworld.generator;

import org.bukkit.Material;

/**
 * Eigene, vereinfachte Biom-Einteilung nur für die Farmwelt.
 * Jedes Farm-Biom hat einen Oberflächenblock und einen eigenen Baumtyp.
 */
public enum FarmBiome {

    PLAINS(Material.GRASS_BLOCK, Material.DIRT),
    FOREST(Material.GRASS_BLOCK, Material.DIRT),
    DESERT(Material.SAND, Material.SANDSTONE),
    TAIGA(Material.SNOW_BLOCK, Material.DIRT),
    SWAMP(Material.MUD, Material.DIRT),
    SAVANNA(Material.GRASS_BLOCK, Material.DIRT);

    private final Material surfaceBlock;
    private final Material subSurfaceBlock;

    FarmBiome(Material surfaceBlock, Material subSurfaceBlock) {
        this.surfaceBlock = surfaceBlock;
        this.subSurfaceBlock = subSurfaceBlock;
    }

    public Material getSurfaceBlock() {
        return surfaceBlock;
    }

    public Material getSubSurfaceBlock() {
        return subSurfaceBlock;
    }

    /**
     * Bestimmt das Farm-Biom für eine Blockspalte anhand einer simplen,
     * hash-basierten Zonen-Einteilung (kachelt die Welt in Zellen).
     */
    public static FarmBiome forColumn(int worldX, int worldZ, int zoneSize) {
        int cellX = Math.floorDiv(worldX, zoneSize);
        int cellZ = Math.floorDiv(worldZ, zoneSize);

        long hash = hash(cellX, cellZ);
        FarmBiome[] values = values();
        int index = (int) Math.floorMod(hash, values.length);
        return values[index];
    }

    private static long hash(int x, int z) {
        long h = x * 341873128712L + z * 132897987541L;
        h ^= (h >>> 33);
        h *= 0xff51afd7ed558ccdL;
        h ^= (h >>> 33);
        return h;
    }
}
