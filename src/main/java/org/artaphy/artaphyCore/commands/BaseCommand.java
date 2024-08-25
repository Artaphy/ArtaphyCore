package org.artaphy.artaphyCore.commands;

import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {
    private final String permission;
    private final int cooldown;
    protected final Map<String, SubCommand> subCommands = new HashMap<>();

    public BaseCommand(String permission, int cooldown) {
        this.permission = "artaphycore.command." + permission;
        this.cooldown = cooldown;
    }


    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(LanguageManager.get("commands.no-permission"));
            return true;
        }

        if (requiresPlayer() && !(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return true;
        }

        if (args.length > 0 && subCommands.containsKey(args[0].toLowerCase())) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (sender.hasPermission(subCommand.getPermission())) {
                subCommand.execute(sender, args);
            } else {
                sender.sendMessage(LanguageManager.get("commands.no-permission"));
            }
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

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream()
                    .filter(subCmd -> sender.hasPermission(subCommands.get(subCmd).getPermission()))
                    .toList();
        } else if (args.length > 1 && subCommands.containsKey(args[0].toLowerCase())) {
            return subCommands.get(args[0].toLowerCase()).tabComplete(sender, args);
        }
        return null;
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

    protected abstract void executeCommand(CommandSender sender, Command command, String label, String[] args);

    protected abstract boolean requiresPlayer();
}
