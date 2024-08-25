package org.artaphy.artaphyCore;

import org.artaphy.artaphyCore.commands.CommandRegistry;
import org.artaphy.artaphyCore.config.ConfigManager;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.artaphy.artaphyCore.listeners.ListenerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArtaphyCore extends JavaPlugin {

    private ConfigManager configManager;
    private LanguageManager languageManager;
    private CommandRegistry commandRegistry;
    private ListenerManager listenerManager;

    @Override
    public void onEnable() {

        if (!isServerCompatible()) {
            getLogger().severe("This plugin is only compatible with Minecraft 1.21 and above!");
            getLogger().severe("Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        configManager = new ConfigManager(this);
        configManager.loadConfig();

        languageManager = new LanguageManager(this);
        languageManager.loadLanguage();

        commandRegistry = new CommandRegistry(this);
        commandRegistry.registerCommands();

        listenerManager = new ListenerManager(this);
        listenerManager.registerListeners();

        getLogger().info("ArtaphyCore plugin has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ArtaphyCore plugin has been successfully disabled!");
    }

    private boolean isServerCompatible() {
        String version = Bukkit.getBukkitVersion();
        String[] versionParts = version.split("-")[0].split("\\.");

        int majorVersion = Integer.parseInt(versionParts[1]);
        return majorVersion >= 21;
    }

    public void reload() {
        configManager.reloadConfig();
        languageManager.loadLanguage();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }
}
