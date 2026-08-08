package com.mike.farmworld.generator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.loot.LootTables;

import java.util.Random;

/**
 * Wird nur für Welten registriert, die mit FarmChunkGenerator erzeugt wurden.
 * Konfiguriert frisch generierte Spawner (Mob-Typ) und Truhen (Loot-Table),
 * da das beim reinen Blocksetzen im Generator/Populator nicht möglich ist.
 */
public class DungeonSetupListener implements Listener {

    private static final EntityType[] HOSTILE_MOBS = {
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER
    };
    private final Random random = new Random();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;
        Chunk chunk = event.getChunk();

        for (org.bukkit.block.state.BlockState state : chunk.getTileEntities()) {
            if (state instanceof CreatureSpawner spawner
                    && spawner.getSpawnedType() == null) {
                EntityType type = HOSTILE_MOBS[random.nextInt(HOSTILE_MOBS.length)];
                spawner.setSpawnedType(type);
                spawner.update(true, false);
            } else if (state instanceof Chest chest) {
                Block block = chest.getBlock();
                if (block.getType() == Material.CHEST) {
                    chest.getBlockInventory().clear();
                    chest.setLootTable(LootTables.SIMPLE_DUNGEON.getLootTable());
                    chest.update(true, false);
                }
            }
        }
    }
}
