package me.paradise.swagcraftsg.commands.cmds.acf;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.feature.border.GameBorder;
import me.paradise.swagcraftsg.feature.deathmatch.DeathmatchListener;
import me.paradise.swagcraftsg.utils.TimeSync;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;

import java.util.concurrent.atomic.AtomicInteger;

@CommandAlias("debug")
public class ACFDebugCommand extends BaseCommand {
    @Default
    @CommandCompletion("@debugging")
    @Description("General debugging command")
    public static void onDebug(Player player, String[] args) {
        player.sendMessage("Debugging command");
    }

    @Subcommand("drop")
    @Description("Drop a testing item")
    public static void onDrop(Player player, String[] args) {
        ItemEntity itemEntity = new ItemEntity(ItemStack.of(Material.BREAD));
        itemEntity.setPickupDelay(5000, TimeUnit.MILLISECOND);
        itemEntity.setInstance(SwagCraftSG.MAIN_INSTANCE);
        itemEntity.spawn();
        itemEntity.teleport(player.getPosition().add(0, 1, 0));
    }

    @Subcommand("timeRemaining")
    @CommandCompletion("@timeRemaining")
    @Description("Time related debugging command")
    public static void onTimeRemaining(Player player, String[] args) {
        player.sendMessage("Time remaining: " + TimeSync.getInstance().getTime());
    }

    @Subcommand("timeRemaining set")
    @Syntax("<time>")
    @Description("Time related debugging command")
    public static void onTimeRemainingSet(Player player, String[] args) {
        if(args.length < 1) {
            player.sendMessage("Usage: /debug timeRemaining set <time>");
            return;
        }

        try {
            int time = Integer.parseInt(args[0]);
            GameBorder.getInstance().setGameProgress(new AtomicInteger(time));
            player.sendMessage("Time remaining to: " + GameBorder.getInstance().getGameProgress());
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("Invalid time", NamedTextColor.RED));
        }
    }

    @Subcommand("deathmatch")
    @Description("Force a deathmatch")
    public static void onDeathMatch(Player player, String[] args) {
        if(TimeSync.getInstance().isDeathmatch()) {
            player.sendMessage(Component.text("Deathmatch already started", NamedTextColor.RED));
            return;
        }
        player.sendMessage("Deathmatch starting...");
        DeathmatchListener.beginDeathMatch();
    }
}
