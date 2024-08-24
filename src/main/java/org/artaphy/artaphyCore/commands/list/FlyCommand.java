package org.artaphy.artaphyCore.commands.list;

import org.artaphy.artaphyCore.commands.BaseCommand;
import org.artaphy.artaphyCore.commands.CommandFlags;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand extends BaseCommand {

    public FlyCommand() {
        super("fly", 0);
    }

    @Override
    protected void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target = null;
        boolean hasPlayerArg = false;

        if (args.length > 0 && !args[0].startsWith("-")) {
            target = Bukkit.getPlayer(args[0]);
            hasPlayerArg = true;
        }

        if (target == null && hasPlayerArg) {
            sender.sendMessage(LanguageManager.get("commands.player-not-found").replace("{player}", args[0]));
            return;
        }

        if (target == null && sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage(LanguageManager.get("commands.player-only"));
            return;
        }

        boolean hasOnFlag = CommandFlags.hasOnFlag(args);
        boolean hasOffFlag = CommandFlags.hasOffFlag(args);

        if (hasOnFlag && hasOffFlag) {
            sender.sendMessage(LanguageManager.get("commands.conflicting-flags"));
            return;
        }

        if (target != sender && !sender.hasPermission("artaphycore.command.fly.others")) {
            sender.sendMessage(LanguageManager.get("commands.fly.no-permission-others"));
            return;
        }

        boolean newFlightState = hasOnFlag || (!hasOffFlag && !target.getAllowFlight());
        target.setAllowFlight(newFlightState);
        target.setFlying(newFlightState);

        String status = LanguageManager.get(newFlightState ? "common.enabled" : "common.disabled");

        if (target == sender) {
            sender.sendMessage(LanguageManager.get("commands.fly.toggle-self").replace("{status}", status));
        } else {
            sender.sendMessage(LanguageManager.get("commands.fly.toggle-other")
                    .replace("{player}", target.getName())
                    .replace("{status}", status));
            target.sendMessage(LanguageManager.get("commands.fly.toggled-by-other").replace("{status}", status));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("artaphycore.command.fly.others")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            completions.add(CommandFlags.ON);
            completions.add(CommandFlags.OFF);
        } else if (args.length == 2) {
            completions.add(CommandFlags.ON);
            completions.add(CommandFlags.OFF);
        }

        return completions;
    }

    @Override
    protected boolean requiresPlayer() {
        return true;
    }
}
