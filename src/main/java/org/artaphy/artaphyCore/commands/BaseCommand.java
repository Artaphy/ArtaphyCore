package org.artaphy.artaphyCore.commands;

import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {
    private final String permission;
    private final int cooldown;

    public BaseCommand(String permission, int cooldown) {
        this.permission = "artaphycore.command." + permission;
        this.cooldown = cooldown;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(LanguageManager.get("commands.no-permission"));
            return true;
        }

        if (requiresPlayer() && !(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return true;
        }

        if (sender instanceof Player player) {
            if (!handleCooldown(player, command)) {
                return true;
            }
        }

        executeCommand(sender, command, label, args);
        return true;
    }

    private boolean handleCooldown(Player player, Command command) {
        UUID uuid = player.getUniqueId();
        CooldownManager cooldownManager = CooldownManager.getInstance();

        if (cooldown > 0 && cooldownManager.isOnCooldown(command.getName(), uuid, cooldown)) {
            long remainingTime = cooldownManager.getRemainingCooldown(command.getName(), uuid, cooldown);
            player.sendMessage(LanguageManager.get("commands.cooldown").replace("{time}", String.valueOf(remainingTime)));
            return false;
        }

        cooldownManager.setCooldown(command.getName(), uuid);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    protected abstract void executeCommand(CommandSender sender, Command command, String label, String[] args);

    protected abstract boolean requiresPlayer();
}
