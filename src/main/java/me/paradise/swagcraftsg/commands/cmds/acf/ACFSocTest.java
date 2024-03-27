package me.paradise.swagcraftsg.commands.cmds.acf;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

@CommandAlias("soctest")
public class ACFSocTest extends BaseCommand {

    @Subcommand("foo")
    public void onFoo1(Player player) {
        player.sendMessage("you foo'd");
    }

    @Subcommand("foo")
    public void onFoo2(CommandSender sender, @Single String foo) {
        sender.sendMessage("You foo'd with " + foo);
    }

    @CatchUnknown
    public void onUnknown(CommandSender sender) {
        sender.sendMessage("UNKNOWN!");
    }

    @Default
    public void test(Player player, String string, @Default("1") int integer) {
        player.sendMessage("Hi " + string + " - " + integer);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}