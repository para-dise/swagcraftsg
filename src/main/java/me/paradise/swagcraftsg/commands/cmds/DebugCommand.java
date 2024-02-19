package me.paradise.swagcraftsg.commands.cmds;

import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import me.paradise.swagcraftsg.match.MatchInvincibilityTimer;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class DebugCommand extends Command {
    public DebugCommand() {
        super("debug");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("This command is a debug command!"));
        });

        var subCommand = ArgumentType.String("action").setSuggestionCallback((sender, context, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("drop"));
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
    }
}
