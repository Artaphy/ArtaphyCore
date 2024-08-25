package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TakeSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(LanguageManager.get("commands.item.take.usage"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(LanguageManager.get("commands.item.take.player-not-found"));
            return;
        }

        String materialName = args[2].toUpperCase();
        Material material;
        try {
            material = Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(LanguageManager.get("commands.item.take.invalid-material"));
            return;
        }

        int amount = 1;
        if (args.length > 3) {
            try {
                amount = Integer.parseInt(args[3]);
                if (amount < 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(LanguageManager.get("commands.item.take.invalid-amount"));
                return;
            }
        }

        int removed = removeItems(target, material, amount);
        if (removed > 0) {
            sender.sendMessage(LanguageManager.get("commands.item.take.success")
                    .replace("{player}", target.getName())
                    .replace("{amount}", String.valueOf(removed))
                    .replace("{material}", material.name().toLowerCase()));
            target.sendMessage(LanguageManager.get("commands.item.take.removed")
                    .replace("{amount}", String.valueOf(removed))
                    .replace("{material}", material.name().toLowerCase()));
        } else {
            sender.sendMessage(LanguageManager.get("commands.item.take.not-found")
                    .replace("{player}", target.getName())
                    .replace("{material}", material.name().toLowerCase()));
        }
    }

    private int removeItems(Player player, Material material, int amount) {
        int removed = 0;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == material) {
                int itemAmount = item.getAmount();
                if (itemAmount <= amount - removed) {
                    removed += itemAmount;
                    contents[i] = null;
                } else {
                    item.setAmount(itemAmount - (amount - removed));
                    removed = amount;
                    break;
                }
            }
            if (removed >= amount) {
                break;
            }
        }
        player.getInventory().setContents(contents);
        return removed;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return null; // 返回null以获取在线玩家列表
        } else if (args.length == 3) {
            String input = args[2].toUpperCase();
            return Arrays.stream(Material.values())
                    .map(Material::name)
                    .filter(name -> name.startsWith(input))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.take";
    }

    @Override
    public String getUsage() {
        return "/item take <player> <material> [amount]";
    }

    @Override
    public String getDescription() {
        return "Take a specified item from a player's inventory";
    }
}
