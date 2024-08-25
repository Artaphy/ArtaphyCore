package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlagSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(LanguageManager.get("commands.item.flag.usage"));
            return;
        }

        String subCommand = args[1].toLowerCase();

        switch (subCommand) {
            case "help":
                sendHelpMessage(sender);
                break;
            case "add":
                addFlag(player, args);
                break;
            case "remove":
                removeFlag(player, args);
                break;
            case "clear":
                clearFlags(player);
                break;
            default:
                sender.sendMessage(LanguageManager.get("commands.item.flag.invalid-subcommand"));
        }
    }

    private void sendHelpMessage(CommandSender sender) {
        List<String> helpMessages = LanguageManager.getList("commands.item.flag.help");
        helpMessages.forEach(sender::sendMessage);
    }

    private void addFlag(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(LanguageManager.get("commands.item.flag.add.usage"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(LanguageManager.get("common.no-item"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(LanguageManager.get("common.no-item-meta"));
            return;
        }

        String flagName = args[2].toUpperCase();
        ItemFlag flag;
        try {
            flag = ItemFlag.valueOf(flagName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(LanguageManager.get("commands.item.flag.invalid-flag").replace("{flag}", flagName));
            return;
        }

        meta.addItemFlags(flag);
        item.setItemMeta(meta);
        player.sendMessage(LanguageManager.get("commands.item.flag.added").replace("{flag}", flagName));
    }

    private void removeFlag(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(LanguageManager.get("commands.item.flag.remove.usage"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(LanguageManager.get("common.no-item"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(LanguageManager.get("common.no-item-meta"));
            return;
        }

        String flagName = args[2].toUpperCase();
        ItemFlag flag;
        try {
            flag = ItemFlag.valueOf(flagName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(LanguageManager.get("commands.item.flag.invalid-flag").replace("{flag}", flagName));
            return;
        }

        if (meta.hasItemFlag(flag)) {
            meta.removeItemFlags(flag);
            item.setItemMeta(meta);
            player.sendMessage(LanguageManager.get("commands.item.flag.removed").replace("{flag}", flagName));
        } else {
            player.sendMessage(LanguageManager.get("commands.item.flag.not-present").replace("{flag}", flagName));
        }
    }

    private void clearFlags(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(LanguageManager.get("common.no-item"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(LanguageManager.get("common.no-item-meta"));
            return;
        }

        meta.removeItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
        player.sendMessage(LanguageManager.get("commands.item.flag.cleared"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("help", "add", "remove", "clear");
        } else if (args.length == 3 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
            String input = args[2].toUpperCase();
            return Arrays.stream(ItemFlag.values())
                    .map(Enum::name)
                    .filter(flag -> flag.startsWith(input))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.flag";
    }

    @Override
    public String getUsage() {
        return "/item flag [help|add|remove|clear]";
    }

    @Override
    public String getDescription() {
        return "Manage ItemFlags on the item in your main hand";
    }
}
