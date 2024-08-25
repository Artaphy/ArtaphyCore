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

public class FeedCommand extends BaseCommand {

    public FeedCommand() {
        super("feed", 0);
    }

    @Override
    protected void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target = null;
        boolean fillSaturation = false;

        for (String arg : args) {
            if (arg.equalsIgnoreCase("-sat")) {
                fillSaturation = true;
            } else if (target == null) {
                target = Bukkit.getPlayer(arg);
            }
        }

        if (target == null && sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage(LanguageManager.get("commands.player-not-found"));
            return;
        }

        if (target != sender && !sender.hasPermission("artaphycore.command.feed.others")) {
            sender.sendMessage(LanguageManager.get("commands.no-permission"));
            return;
        }

        target.setFoodLevel(20);
        if (fillSaturation) {
            target.setSaturation(20f);
        }

        String message = fillSaturation ? "commands.feed.fed-and-saturated" : "commands.feed.fed";

        if (target == sender) {
            sender.sendMessage(LanguageManager.get(message + "-self"));
        } else {
            sender.sendMessage(LanguageManager.get(message + "-other").replace("{player}", target.getName()));
            target.sendMessage(LanguageManager.get(message + "-by-other"));
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("artaphycore.command.feed.others")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            completions.add("-sat");
        } else if (args.length == 2) {
            completions.add("-sat");
        }

        return completions;
    }

    @Override
    protected boolean requiresPlayer() {
        return false;
    }
}
