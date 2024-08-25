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

public class SudoCommand extends BaseCommand {

    public SudoCommand() {
        super("sudo", 0);
    }

    @Override
    protected void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(LanguageManager.get("commands.sudo.usage"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(LanguageManager.get("commands.player-not-found").replace("{player}", args[0]));
            return;
        }

        String sudoCommand = args[1];
        boolean isChatMessage = false;

        if (sudoCommand.startsWith("\"") && sudoCommand.endsWith("\"")) {
            sudoCommand = sudoCommand.substring(1, sudoCommand.length() - 1);
        } else {
            sender.sendMessage(LanguageManager.get("commands.sudo.invalid-format"));
            return;
        }

        if (!sudoCommand.startsWith("/")) {
            isChatMessage = true;
        } else if (args.length > 2 && args[2].equalsIgnoreCase("-c")) {
            isChatMessage = true;
            sudoCommand = sudoCommand.substring(1);
        }

        if (isChatMessage) {
            target.chat(sudoCommand);
            sender.sendMessage(LanguageManager.get("commands.sudo.success-chat")
                    .replace("{player}", target.getName())
                    .replace("{message}", sudoCommand));
        } else {
            target.performCommand(sudoCommand.substring(1)); // 移除开头的 "/"
            sender.sendMessage(LanguageManager.get("commands.sudo.success-command")
                    .replace("{player}", target.getName())
                    .replace("{command}", sudoCommand));
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 3 && args[1].startsWith("\"/")) {
            completions.add("-c");
        }

        return completions;
    }

    @Override
    protected boolean requiresPlayer() {
        return false;
    }
}
