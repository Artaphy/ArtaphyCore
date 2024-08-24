package org.artaphy.artaphyCore.config;

import org.artaphy.artaphyCore.ArtaphyCore;

public class ConfigManager {
    private final ArtaphyCore plugin;

    public ConfigManager(ArtaphyCore plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        plugin.getLanguageManager().loadLanguage();
    }

    public String getString(String path) {
        return plugin.getConfig().getString(path);
    }

    public int getInt(String path) {
        return plugin.getConfig().getInt(path);
    }

    public boolean getBoolean(String path) {
        return plugin.getConfig().getBoolean(path);
    }
}
