package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UnbreakableSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(LanguageManager.get("commands.item.no-item"));
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            boolean isUnbreakable = !meta.isUnbreakable();
            meta.setUnbreakable(isUnbreakable);
            item.setItemMeta(meta);
            String status = LanguageManager.get(isUnbreakable ? "common.enabled" : "common.disabled");
            player.sendMessage(LanguageManager.get("commands.item.unbreakable").replace("{status}", status));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.unbreakable";
    }

    @Override
    public String getUsage() {
        return "/item unbreakable";
    }

    @Override
    public String getDescription() {
        return LanguageManager.get("commands.item.unbreakable.description");
    }
}
