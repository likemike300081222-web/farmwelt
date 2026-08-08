package com.mike.farmworld;

import com.mike.farmworld.commands.FarmWorldCommand;
import com.mike.farmworld.generator.DungeonSetupListener;
import org.bukkit.plugin.java.JavaPlugin;

public class FarmWorldPlugin extends JavaPlugin {

    private static FarmWorldPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        FarmWorldCommand executor = new FarmWorldCommand(this);
        getCommand("farmworld").setExecutor(executor);
        getCommand("farmworld").setTabCompleter(executor);

        // Global registriert; der Listener greift nur bei frisch generierten
        // Chunks mit Spawnern/Truhen, was außerhalb von Farmwelten selten vorkommt.
        getServer().getPluginManager().registerEvents(new DungeonSetupListener(), this);

        getLogger().info("FarmWorld aktiviert. Nutze /farmworld create <name> um eine Farmwelt zu erzeugen.");
    }

    @Override
    public void onDisable() {
        getLogger().info("FarmWorld deaktiviert.");
    }

    public static FarmWorldPlugin getInstance() {
        return instance;
    }
}
