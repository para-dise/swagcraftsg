package me.paradise.swagcraftsg.commands.cmds;

import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.feature.border.GameBorder;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandData;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;

import java.util.concurrent.atomic.AtomicInteger;

public class DebugCommand extends Command {
    public DebugCommand() {
        super("debug");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("This command is a debug command!"));
        });

        var subCommand = ArgumentType.String("action").setSuggestionCallback((sender, context, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("drop"));
            suggestion.addEntry(new SuggestionEntry("timeRemaining"));
        });

        addSyntax((sender, context) -> {
            final String action = context.get(subCommand);
            if(action.equals("drop")) {
               Player player = (Player) sender;

                ItemEntity itemEntity = new ItemEntity(ItemStack.of(Material.BREAD));
                itemEntity.setPickupDelay(5000, TimeUnit.MILLISECOND);
                itemEntity.setInstance(SwagCraftSG.MAIN_INSTANCE);
                itemEntity.spawn();
                itemEntity.teleport(player.getPosition().add(0, 1, 0));
            }
        }, subCommand);

        // TimeRemaining subcommand
        Command timeRemainingSubcommand = new Command("timeRemaining");
        timeRemainingSubcommand.setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("This is a subcommand!"));
        });

        var subSubCommand = ArgumentType.String("set").setSuggestionCallback((sender, context, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("set"));
        });
        var numberArgument = ArgumentType.Integer("time-remaining");

        timeRemainingSubcommand.addSyntax((sender, context) -> {
            final int time = context.get(numberArgument);
            GameBorder.getInstance().setGameProgress(new AtomicInteger(time));

            sender.sendMessage(Component.text("Setting remaining time to " + time));
        }, subSubCommand, numberArgument);

        this.addSubcommand(timeRemainingSubcommand);
    }
}
