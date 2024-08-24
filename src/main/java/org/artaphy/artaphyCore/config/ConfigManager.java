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
}
