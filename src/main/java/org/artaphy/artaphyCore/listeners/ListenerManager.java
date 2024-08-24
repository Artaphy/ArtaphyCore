package org.artaphy.artaphyCore.listeners;

import org.artaphy.artaphyCore.ArtaphyCore;
import org.bukkit.event.Listener;

public class ListenerManager {
    private final ArtaphyCore plugin;

    public ListenerManager(ArtaphyCore plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {

    }

    public void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
