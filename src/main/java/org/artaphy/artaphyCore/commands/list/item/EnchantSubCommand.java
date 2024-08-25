package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(LanguageManager.get("commands.item.enchant.usage"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(LanguageManager.get("common.no-item"));
            return;
        }

        String enchantName = args[1].toLowerCase();
        Enchantment enchantment = Registry.ENCHANTMENT.get(org.bukkit.NamespacedKey.minecraft(enchantName));
        if (enchantment == null) {
            sender.sendMessage(LanguageManager.get("commands.item.enchant.invalid-enchant").replace("{enchant}", enchantName));
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(LanguageManager.get("commands.item.enchant.invalid-level").replace("{level}", args[2]));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (level <= 0) {
                meta.removeEnchant(enchantment);
                sender.sendMessage(LanguageManager.get("commands.item.enchant.removed")
                        .replace("{enchant}", enchantment.getKey().getKey()));
            } else {
                meta.addEnchant(enchantment, level, true);
                sender.sendMessage(LanguageManager.get("commands.item.enchant.success")
                        .replace("{enchant}", enchantment.getKey().getKey())
                        .replace("{level}", String.valueOf(level)));
            }
            item.setItemMeta(meta);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> enchantments = new ArrayList<>();
            for (Enchantment enchantment : Registry.ENCHANTMENT) {
                enchantments.add(enchantment.getKey().getKey());
            }
            return enchantments;
        } else if (args.length == 3) {
            return List.of("1", "2", "3", "4", "5");
        }
        return null;
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.enchant";
    }

    @Override
    public String getUsage() {
        return "/item enchant <enchantment> <level>";
    }

    @Override
    public String getDescription() {
        return "Enchants the item in your main hand";
    }
}
