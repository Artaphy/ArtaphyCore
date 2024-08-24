package org.artaphy.artaphyCore.utils;

import org.bukkit.ChatColor;

public class ColorUtil {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message.replaceAll("&\\{#([A-Fa-f0-9]{6})}", "§x§$1"));
    }
}
