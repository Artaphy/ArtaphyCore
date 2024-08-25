package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class AmountSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(LanguageManager.get("commands.item.amount.usage"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(LanguageManager.get("commands.item.no-item"));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(LanguageManager.get("commands.item.amount.invalid-number"));
            return;
        }

        if (amount < 1 || amount > item.getMaxStackSize()) {
            sender.sendMessage(LanguageManager.get("commands.item.amount.invalid-amount")
                    .replace("{max}", String.valueOf(item.getMaxStackSize())));
            return;
        }

        item.setAmount(amount);
        sender.sendMessage(LanguageManager.get("commands.item.amount.success")
                .replace("{amount}", String.valueOf(amount)));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Collections.singletonList("<amount>");
        }
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.amount";
    }

    @Override
    public String getUsage() {
        return "/item amount <amount>";
    }

    @Override
    public String getDescription() {
        return "Set the amount of the item in your main hand";
    }
}
