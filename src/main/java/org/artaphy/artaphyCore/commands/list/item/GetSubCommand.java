package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GetSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.get("commands.only-player"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(LanguageManager.get("commands.item.get.usage"));
            return;
        }

        Player player = (Player) sender;
        Material material = Material.matchMaterial(args[1]);
        if (material == null) {
            sender.sendMessage(LanguageManager.get("commands.item.get.invalid-material"));
            return;
        }

        int amount = 1;
        if (args.length > 2) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(LanguageManager.get("commands.item.get.invalid-amount"));
                return;
            }
        }

        ItemStack item = new ItemStack(material, amount);
        player.getInventory().addItem(item);
        sender.sendMessage(LanguageManager.get("commands.item.get.success")
                .replace("{amount}", String.valueOf(amount))
                .replace("{material}", material.name().toLowerCase()));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            for (Material material : Material.values()) {
                if (material.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(material.name());
                }
            }
        } else if (args.length == 3) {
            completions.add("1");
            completions.add("64");
        }
        return completions;
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.get";
    }

    @Override
    public String getUsage() {
        return "/item get <material> [amount]";
    }

    @Override
    public String getDescription() {
        return "Get a specified item";
    }
}
