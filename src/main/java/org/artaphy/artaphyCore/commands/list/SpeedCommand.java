package org.artaphy.artaphyCore.commands.list;

import org.artaphy.artaphyCore.ArtaphyCore;
import org.artaphy.artaphyCore.commands.BaseCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand extends BaseCommand {

    public SpeedCommand() {
        super("speed", 0);
    }

    @Override
    protected void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(LanguageManager.get("commands.speed.usage"));
            return;
        }

        float speed;
        try {
            speed = Float.parseFloat(args[0]);
            if (speed < 0 || speed > 10) {
                sender.sendMessage(LanguageManager.get("commands.speed.invalid-range"));
                return;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(LanguageManager.get("commands.speed.invalid-number"));
            return;
        }

        Player target;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(LanguageManager.get("commands.player-not-found").replace("{player}", args[1]));
                return;
            }
            if (!sender.hasPermission("artaphycore.command.speed.others")) {
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
        target.setWalkSpeed(normalizedSpeed);

        if (target == sender) {
            sender.sendMessage(LanguageManager.get("commands.speed.set-self")
                    .replace("{speed}", String.format("%.1f", speed)));
        } else {
            sender.sendMessage(LanguageManager.get("commands.speed.set-other")
                    .replace("{player}", target.getName())
                    .replace("{speed}", String.format("%.1f", speed)));
            target.sendMessage(LanguageManager.get("commands.speed.set-by-other")
                    .replace("{speed}", String.format("%.1f", speed)));
        }
    }

    @Override
    protected boolean requiresPlayer() {
        return true;
    }
}
