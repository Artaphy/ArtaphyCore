package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GiveSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(LanguageManager.get("commands.item.give.usage"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(LanguageManager.get("commands.player-not-found"));
            return;
        }

        Material material = Material.matchMaterial(args[2]);
        if (material == null) {
            sender.sendMessage(LanguageManager.get("commands.invalid-material"));
            return;
        }

        int amount = 1;
        if (args.length > 3) {
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(LanguageManager.get("commands.item.give.invalid-amount"));
                return;
            }
        }

        ItemStack item = new ItemStack(material, amount);
        target.getInventory().addItem(item);
        sender.sendMessage(LanguageManager.get("commands.item.give.success")
                .replace("{amount}", String.valueOf(amount))
                .replace("{material}", material.name().toLowerCase())
                .replace("{player}", target.getName()));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            return null;
        } else if (args.length == 3) {
            for (Material material : Material.values()) {
                if (material.name().toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add(material.name());
                }
            }
        } else if (args.length == 4) {
            completions.add("1");
            completions.add("64");
        }
        return completions;
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.give";
    }

    @Override
    public String getUsage() {
        return "/item give <player> <material> [amount]";
    }

    @Override
    public String getDescription() {
        return "Give a specified item to a player";
    }
}
