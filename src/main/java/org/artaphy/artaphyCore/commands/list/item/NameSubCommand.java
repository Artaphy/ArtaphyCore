package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NameSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(LanguageManager.get("common.no-item"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            sender.sendMessage(LanguageManager.get("common.no-item-meta"));
            return;
        }

        if (args.length < 2) {
            meta.setDisplayName(null);
            item.setItemMeta(meta);
            sender.sendMessage(LanguageManager.get("commands.item.name.reset"));
            return;
        }

        String newName = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        meta.setDisplayName(newName);
        item.setItemMeta(meta);
        sender.sendMessage(LanguageManager.get("commands.item.name.success").replace("{name}", newName));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.name";
    }

    @Override
    public String getUsage() {
        return "/item name [name]";
    }

    @Override
    public String getDescription() {
        return "Changes the name of the item in your main hand";
    }
}
