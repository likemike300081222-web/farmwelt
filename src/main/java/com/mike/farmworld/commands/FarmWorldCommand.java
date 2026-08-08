package com.mike.farmworld.commands;

import com.mike.farmworld.FarmWorldPlugin;
import com.mike.farmworld.generator.FarmChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FarmWorldCommand implements CommandExecutor, TabCompleter {

    private final FarmWorldPlugin plugin;

    public FarmWorldCommand(FarmWorldPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                              @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eNutzung: /farmworld <create|tp|list> [name]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cBitte einen Weltnamen angeben: /farmworld create <name>");
                    return true;
                }
                createWorld(sender, args[1]);
            }
            case "tp" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cBitte einen Weltnamen angeben: /farmworld tp <name>");
                    return true;
                }
                teleport(sender, args[1]);
            }
            case "list" -> {
                sender.sendMessage("§aGeladene Welten: §7" +
                        Bukkit.getWorlds().stream().map(World::getName).reduce((a, b) -> a + ", " + b).orElse("-"));
            }
            default -> sender.sendMessage("§eNutzung: /farmworld <create|tp|list> [name]");
        }
        return true;
    }

    private void createWorld(CommandSender sender, String name) {
        if (Bukkit.getWorld(name) != null) {
            sender.sendMessage("§cEine Welt mit diesem Namen existiert bereits.");
            return;
        }

        int zoneSize = plugin.getConfig().getInt("biome-zone-size", 200);

        sender.sendMessage("§aErstelle Farmwelt '" + name + "' ... das kann einen Moment dauern.");

        WorldCreator creator = new WorldCreator(name)
                .generator(new FarmChunkGenerator(zoneSize))
                .type(WorldType.FLAT)
                .generateStructures(false);

        World world = creator.createWorld();

        if (world == null) {
            sender.sendMessage("§cWelt konnte nicht erstellt werden.");
            return;
        }

        world.setSpawnFlags(true, true);
        sender.sendMessage("§aFarmwelt '" + name + "' wurde erstellt! Mit /farmworld tp " + name + " kannst du hin.");
    }

    private void teleport(CommandSender sender, String name) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können teleportiert werden.");
            return;
        }

        World world = Bukkit.getWorld(name);
        if (world == null) {
            sender.sendMessage("§cDiese Welt ist nicht geladen. Existiert sie? (/farmworld list)");
            return;
        }

        player.teleport(world.getSpawnLocation());
        player.sendMessage("§aWillkommen in der Farmwelt '" + name + "'!");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                  @NotNull String alias, String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            options.addAll(List.of("create", "tp", "list"));
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("tp"))) {
            for (World w : Bukkit.getWorlds()) options.add(w.getName());
        }
        return options;
    }
}
