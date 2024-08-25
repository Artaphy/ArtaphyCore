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

public class LoreSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(LanguageManager.get("commands.item.lore.usage"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(LanguageManager.get("commands.item.no-item"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(LanguageManager.get("commands.item.no-meta"));
            return;
        }

        String action = args[1].toLowerCase();
        switch (action) {
            case "help":
                List<String> helpMessages = LanguageManager.getList("commands.item.lore.help");
                for (String message : helpMessages) {
                    sender.sendMessage(message);
                }
            case "add":
                handleAddLore(player, meta, args);
                break;
            case "remove":
                handleRemoveLore(player, meta, args);
                break;
            case "set":
                handleSetLore(player, meta, args);
                break;
            case "clear":
                handleClearLore(player, meta);
                break;
            default:
                player.sendMessage(LanguageManager.get("commands.item.lore.invalid-action"));
        }

        item.setItemMeta(meta);
    }

    private void handleAddLore(Player player, ItemMeta meta, String[] args) {
        if (args.length < 3) {
            player.sendMessage(LanguageManager.get("commands.item.lore.add.usage"));
            return;
        }
        String lore = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        List<String> loreList = meta.getLore();
        if (loreList == null) {
            loreList = new ArrayList<>();
        }
        loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
        meta.setLore(loreList);
        player.sendMessage(LanguageManager.get("commands.item.lore.add.success"));
    }

    private void handleRemoveLore(Player player, ItemMeta meta, String[] args) {
        if (args.length < 3) {
            player.sendMessage(LanguageManager.get("commands.item.lore.remove.usage"));
            return;
        }
        int index;
        try {
            index = Integer.parseInt(args[2]) - 1;
        } catch (NumberFormatException e) {
            player.sendMessage(LanguageManager.get("commands.item.lore.invalid-index"));
            return;
        }
        List<String> loreList = meta.getLore();
        if (loreList == null || index < 0 || index >= loreList.size()) {
            player.sendMessage(LanguageManager.get("commands.item.lore.index-out-of-bounds"));
            return;
        }
        loreList.remove(index);
        meta.setLore(loreList);
        player.sendMessage(LanguageManager.get("commands.item.lore.remove.success"));
    }

    private void handleSetLore(Player player, ItemMeta meta, String[] args) {
        if (args.length < 3) {
            player.sendMessage(LanguageManager.get("commands.item.lore.set.usage"));
            return;
        }
        List<String> loreList = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', args[i]));
        }
        meta.setLore(loreList);
        player.sendMessage(LanguageManager.get("commands.item.lore.set.success"));
    }

    private void handleClearLore(Player player, ItemMeta meta) {
        meta.setLore(null);
        player.sendMessage(LanguageManager.get("commands.item.lore.clear.success"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("add", "remove", "set", "clear");
        }
        return null;
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.";
    }

    @Override
    public String getUsage() {
        return "/item lore [add|remove|clear|help]";
    }

    @Override
    public String getDescription() {
        return "";
    }
}
