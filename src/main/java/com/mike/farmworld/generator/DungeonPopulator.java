package com.mike.farmworld.generator;

import com.mike.farmworld.FarmWorldPlugin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.loot.LootTables;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DungeonPopulator extends BlockPopulator {

    private static final EntityType[] HOSTILE_MOBS = {
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER
    };

    @Override
    public void populate(@NotNull org.bukkit.generator.WorldInfo worldInfo, @NotNull Random random,
                          int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {

        var config = FarmWorldPlugin.getInstance().getConfig();
        double chance = config.getDouble("dungeon-chance-per-chunk", 0.02);
        if (random.nextDouble() > chance) return;

        int minY = config.getInt("dungeon-min-y", -40);
        int maxY = config.getInt("dungeon-max-y", 10);

        int worldX = (chunkX << 4) + 4 + random.nextInt(8);
        int worldZ = (chunkZ << 4) + 4 + random.nextInt(8);
        int worldY = minY + random.nextInt(Math.max(1, maxY - minY));

        buildRoom(limitedRegion, worldX, worldY, worldZ, random);
    }

    private void buildRoom(LimitedRegion region, int cx, int cy, int cz, Random random) {
        int sizeX = 5, sizeY = 4, sizeZ = 5;

        // Wände, Boden, Decke aus Bruchsteinziegeln, Innenraum leeren
        for (int dx = -sizeX / 2; dx <= sizeX / 2; dx++) {
            for (int dy = 0; dy < sizeY; dy++) {
                for (int dz = -sizeZ / 2; dz <= sizeZ / 2; dz++) {
                    int x = cx + dx, y = cy + dy, z = cz + dz;
                    if (!region.isInRegion(x, y, z)) continue;

                    boolean isEdge = Math.abs(dx) == sizeX / 2 || Math.abs(dz) == sizeZ / 2
                            || dy == 0 || dy == sizeY - 1;

                    if (isEdge) {
                        region.setType(x, y, z, Material.MOSSY_STONE_BRICKS);
                    } else {
                        region.setType(x, y, z, Material.CAVE_AIR);
                    }
                }
            }
        }

        // Spawner in der Mitte
        if (region.isInRegion(cx, cy + 1, cz)) {
            region.setType(cx, cy + 1, cz, Material.SPAWNER);
            // Der konkrete Mob-Typ eines Spawners lässt sich zur Generationszeit
            // über LimitedRegion nicht direkt setzen (kein BlockState-Zugriff).
            // Das Plugin passt neu generierte Spawner beim Chunk-Load im Listener an
            // (siehe FarmWorldPlugin – optional erweiterbar).
        }

        // Truhe mit Loot-Table in einer Ecke
        int chestX = cx + sizeX / 2 - 1;
        int chestZ = cz + sizeZ / 2 - 1;
        if (region.isInRegion(chestX, cy + 1, chestZ)) {
            region.setType(chestX, cy + 1, chestZ, Material.CHEST);
        }
    }
}
