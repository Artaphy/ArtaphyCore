package org.artaphy.artaphyCore.commands.list;

import org.artaphy.artaphyCore.commands.BaseCommand;
import org.artaphy.artaphyCore.commands.list.item.*;
import org.artaphy.artaphyCore.config.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ItemCommand extends BaseCommand {

    public ItemCommand() {
        super("item", 0);
        registerSubCommands();
    }

    private void registerSubCommands() {
        subCommands.put("amount", new AmountSubCommand());
        subCommands.put("damage", new DamageSubCommand());
        subCommands.put("enchant", new EnchantSubCommand());
        subCommands.put("flag", new FlagSubCommand());
        subCommands.put("get", new GetSubCommand());
        subCommands.put("give", new GiveSubCommand());
        subCommands.put("help", new HelpSubCommand());
        subCommands.put("lore", new LoreSubCommand());
        subCommands.put("name", new NameSubCommand());
        subCommands.put("spawn", new SpawnSubCommand());
        subCommands.put("take", new TakeSubCommand());
        subCommands.put("unbreakable", new UnbreakableSubCommand());
        subCommands.put("potion", new PotionSubCommand());
    }

    @Override
    protected void executeCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(LanguageManager.get("commands.item.help"));
            return;
        }

        sender.sendMessage(LanguageManager.get("commands.item.unknown-subcommand"));
    }

    @Override
    protected boolean requiresPlayer() {
        return false;
    }
}
