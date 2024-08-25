package org.artaphy.artaphyCore.commands.list;

import org.artaphy.artaphyCore.commands.BaseCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FlySpeedCommand extends BaseCommand {

    public FlySpeedCommand() {
        super("flyspeed", 0);
    }

    @Override
    protected void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(LanguageManager.get("commands.flyspeed.usage"));
            return;
        }

        float speed;
        try {
            speed = Float.parseFloat(args[0]);
            if (speed < 0 || speed > 10) {
                sender.sendMessage(LanguageManager.get("commands.flyspeed.invalid-range"));
                return;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(LanguageManager.get("commands.flyspeed.invalid-number"));
            return;
        }

        Player target;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(LanguageManager.get("commands.player-not-found").replace("{player}", args[1]));
                return;
            }
            if (!sender.hasPermission("artaphycore.command.flyspeed.others")) {
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

        float normalizedSpeed = speed / 10f;
        target.setFlySpeed(normalizedSpeed);

        if (target == sender) {
            sender.sendMessage(LanguageManager.get("commands.flyspeed.set-self")
                    .replace("{speed}", String.format("%.1f", speed)));
        } else {
            sender.sendMessage(LanguageManager.get("commands.flyspeed.set-other")
                    .replace("{player}", target.getName())
                    .replace("{speed}", String.format("%.1f", speed)));
            target.sendMessage(LanguageManager.get("commands.flyspeed.set-by-other")
                    .replace("{speed}", String.format("%.1f", speed)));
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (int i = 0; i <= 10; i++) {
                completions.add(String.valueOf(i));
            }
        } else if (args.length == 2 && sender.hasPermission("artaphycore.command.flyspeed.others")) {
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
