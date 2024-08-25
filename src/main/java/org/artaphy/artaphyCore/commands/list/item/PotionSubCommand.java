package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PotionSubCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.usage"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.player-not-found"));
            return;
        }

        ItemStack item = target.getInventory().getItemInMainHand();
        if (!(item.getItemMeta() instanceof PotionMeta potionMeta)) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.not-potion"));
            return;
        }

        String action = args[2].toLowerCase();
        switch (action) {
            case "add":
                handleAddPotion(sender, target, potionMeta, args);
                break;
            case "remove":
                handleRemovePotion(sender, target, potionMeta, args);
                break;
            case "clear":
                handleClearPotions(sender, target, potionMeta);
                break;
            default:
                sender.sendMessage(LanguageManager.get("commands.item.potion.invalid-action"));
        }

        item.setItemMeta(potionMeta);
    }

    private void handleAddPotion(CommandSender sender, Player target, PotionMeta potionMeta, String[] args) {
        if (args.length < 6) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.add.usage"));
            return;
        }

        PotionEffectType effectType = getPotionEffectType(args[3]);
        if (effectType == null) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.invalid-effect"));
            return;
        }

        int duration, amplifier;
        try {
            duration = Integer.parseInt(args[4]);
            amplifier = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.invalid-numbers"));
            return;
        }

        potionMeta.addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
        sender.sendMessage(LanguageManager.get("commands.item.potion.add.success")
                .replace("{player}", target.getName())
                .replace("{effect}", effectType.toString())
                .replace("{duration}", String.valueOf(duration))
                .replace("{amplifier}", String.valueOf(amplifier)));
        target.sendMessage(LanguageManager.get("commands.item.potion.add.changed")
                .replace("{effect}", effectType.toString()));
    }

    private void handleRemovePotion(CommandSender sender, Player target, PotionMeta potionMeta, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.remove.usage"));
            return;
        }

        PotionEffectType effectType = getPotionEffectType(args[3]);
        if (effectType == null) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.invalid-effect"));
            return;
        }

        if (potionMeta.removeCustomEffect(effectType)) {
            sender.sendMessage(LanguageManager.get("commands.item.potion.remove.success")
                    .replace("{player}", target.getName())
                    .replace("{effect}", effectType.toString()));
            target.sendMessage(LanguageManager.get("commands.item.potion.remove.changed")
                    .replace("{effect}", effectType.toString()));
        } else {
            sender.sendMessage(LanguageManager.get("commands.item.potion.remove.not-found")
                    .replace("{effect}", effectType.toString()));
        }
    }

    private void handleClearPotions(CommandSender sender, Player target, PotionMeta potionMeta) {
        potionMeta.clearCustomEffects();
        sender.sendMessage(LanguageManager.get("commands.item.potion.clear.success")
                .replace("{player}", target.getName()));
        target.sendMessage(LanguageManager.get("commands.item.potion.clear.changed"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            return null;
        } else if (args.length == 3) {
            completions.add("add");
            completions.add("remove");
            completions.add("clear");
        } else if (args.length == 4 && (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove"))) {
            for (PotionEffectType type : getAvailablePotionEffects()) {
                completions.add(getEffectName(type).toLowerCase());
            }
        }
        return completions;
    }

    private PotionEffectType getPotionEffectType(String name) {
        try {
            return (PotionEffectType) PotionEffectType.class.getField(name.toUpperCase()).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    private List<PotionEffectType> getAvailablePotionEffects() {
        List<PotionEffectType> effects = new ArrayList<>();
        Field[] fields = PotionEffectType.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == PotionEffectType.class) {
                try {
                    PotionEffectType effect = (PotionEffectType) field.get(null);
                    if (effect != null) {
                        effects.add(effect);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return effects;
    }

    private String getEffectName(PotionEffectType type) {
        NamespacedKey key = type.getKey();
        return key.getKey().replace('_', ' ').toLowerCase();
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.potion";
    }

    @Override
    public String getUsage() {
        return "/item potion <player> <add|remove|clear> [effect] [duration] [amplifier]";
    }

    @Override
    public String getDescription() {
        return "Add, remove, or clear potion effects on an item";
    }
}