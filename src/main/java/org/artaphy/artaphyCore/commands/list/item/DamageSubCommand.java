package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DamageSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(LanguageManager.get("commands.item.damage.usage"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(LanguageManager.get("commands.item.no-item"));
            return;
        }

        try {
            int damageAmount = Integer.parseInt(args[1]);
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable damageable) {
                int maxDurability = item.getType().getMaxDurability();
                int currentDamage = damageable.getDamage();
                int newDamage = Math.min(maxDurability, currentDamage + damageAmount);

                damageable.setDamage(newDamage);
                item.setItemMeta(meta);

                int remainingDurability = maxDurability - newDamage;
                player.sendMessage(LanguageManager.get("commands.item.damage.success")
                        .replace("{amount}", String.valueOf(damageAmount))
                        .replace("{durability}", String.valueOf(remainingDurability)));
            } else {
                player.sendMessage(LanguageManager.get("commands.item.damage.not-damageable"));
            }
        } catch (NumberFormatException e) {
            player.sendMessage(LanguageManager.get("commands.item.damage.invalid-amount"));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("1", "5", "10");
        }
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.damage";
    }

    @Override
    public String getUsage() {
        return "/item damage <amount>";
    }

    @Override
    public String getDescription() {
        return LanguageManager.get("commands.item.damage.description");
    }
}
