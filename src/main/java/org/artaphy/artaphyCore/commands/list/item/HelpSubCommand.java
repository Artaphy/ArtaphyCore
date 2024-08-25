package org.artaphy.artaphyCore.commands.list.item;

import org.artaphy.artaphyCore.commands.SubCommand;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> helpMessages = LanguageManager.getList("commands.item.help");
        for (String message : helpMessages) {
            sender.sendMessage(message);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermission() {
        return "artaphycore.command.item.help";
    }

    @Override
    public String getUsage() {
        return "/item help";
    }

    @Override
    public String getDescription() {
        return "Shows help information for the item command";
    }
}
