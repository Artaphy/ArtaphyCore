package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(LanguageManager.get("commands.item.spawn.usage"));
            return;
        }

        String materialName = args[1].toUpperCase();
        Material material;
        try {
            material = Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(LanguageManager.get("commands.item.spawn.invalid-material"));
            return;
        }

        int amount = 1;
        if (args.length > 2) {
            try {
                amount = Integer.parseInt(args[2]);
                if (amount < 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(LanguageManager.get("commands.item.spawn.invalid-amount"));
                return;
            }
        }

        ItemStack item = new ItemStack(material, amount);
        Location spawnLocation = player.getLocation();
        player.getWorld().dropItem(spawnLocation, item);
        sender.sendMessage(LanguageManager.get("commands.item.spawn.success")
                .replace("{amount}", String.valueOf(amount))
                .replace("{material}", material.name().toLowerCase()));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            String input = args[1].toUpperCase();
            return Arrays.stream(Material.values())
                    .map(Material::name)
                    .filter(name -> name.startsWith(input))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.spawn";
    }

    @Override
    public String getUsage() {
        return "/item spawn <material> [amount]";
    }

    @Override
    public String getDescription() {
        return "Spawn a specified item at your location";
    }
}
