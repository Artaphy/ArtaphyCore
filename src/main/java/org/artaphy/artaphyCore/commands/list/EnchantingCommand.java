package org.artaphy.artaphyCore.commands.list;

import org.artaphy.artaphyCore.commands.BaseCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class EnchantingCommand extends BaseCommand {

    public EnchantingCommand() {
        super("enchanting", 5); // 5秒冷却时间
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
            if (!sender.hasPermission("artaphycore.command.enchanting.others")) {
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

        target.openInventory(Bukkit.createInventory(target, InventoryType.ENCHANTING));

        if (target == sender) {
            sender.sendMessage(LanguageManager.get("commands.enchanting.opened-self"));
        } else {
            sender.sendMessage(LanguageManager.get("commands.enchanting.opened-other").replace("{player}", target.getName()));
            target.sendMessage(LanguageManager.get("commands.enchanting.opened-by-other"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1 && sender.hasPermission("artaphycore.command.enchanting.others")) {
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
