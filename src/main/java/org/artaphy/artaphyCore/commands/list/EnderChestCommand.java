package org.artaphy.artaphyCore.commands.list;

import org.artaphy.artaphyCore.commands.BaseCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnderChestCommand extends BaseCommand {
    public EnderChestCommand() {
        super("enderchest", 5); // 5秒冷却时间
    }

    @Override
    protected void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;

        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(LanguageManager.get("commands.player-not-found").replace("{player}", args[0]));
                return;
            }
            if (!sender.hasPermission("artaphycore.command.enderchest.others")) {
                sender.sendMessage(LanguageManager.get("commands.no-permission"));
                return;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LanguageManager.get("commands.player-only"));
                return;
            }
            target = (Player) sender;
        }

        if (sender instanceof Player playerSender) {
            playerSender.openInventory(target.getEnderChest());

            if (target == sender) {
                sender.sendMessage(LanguageManager.get("commands.enderchest.opened-self"));
            } else {
                sender.sendMessage(LanguageManager.get("commands.enderchest.opened-other").replace("{player}", target.getName()));
            }
        } else {
            sender.sendMessage(LanguageManager.get("commands.enderchest.console-error"));
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1 && sender.hasPermission("artaphycore.command.enderchest.others")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        return completions;
    }

    @Override
    protected boolean requiresPlayer() {
        return true;
    }
}
