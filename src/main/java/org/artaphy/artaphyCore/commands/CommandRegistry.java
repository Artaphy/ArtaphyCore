package org.artaphy.artaphyCore.commands;

import org.artaphy.artaphyCore.ArtaphyCore;
import org.artaphy.artaphyCore.commands.list.*;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CommandRegistry {
    private final ArtaphyCore plugin;

    public CommandRegistry(ArtaphyCore plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        registerCommand("anvil", new AnvilCommand());
        registerCommand("enchanting", new EnchantingCommand());
        registerCommand("enderchest", new EnderChestCommand());
        registerCommand("feed", new FeedCommand());
        registerCommand("fly", new FlyCommand());
        registerCommand("flyspeed", new FlySpeedCommand());
        registerCommand("heal", new HealCommand());
        registerCommand("speed", new SpeedCommand());
        registerCommand("suicide", new SuicideCommand());
        registerCommand("workbench", new WorkbenchCommand());
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = plugin.getCommand(name);
        if (command != null) {
            command.setExecutor(executor);
            plugin.getLogger().info("Succeeded to register command: " + name);
            if (executor instanceof TabCompleter) command.setTabCompleter((TabCompleter) executor);
        } else {
            plugin.getLogger().warning("Failed to register command: " + name);
        }
    }
}
