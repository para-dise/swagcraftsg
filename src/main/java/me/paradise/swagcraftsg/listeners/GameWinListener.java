package me.paradise.swagcraftsg.listeners;

import me.paradise.swagcraftsg.events.GameWinEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

public class GameWinListener {
    public GameWinListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(GameWinEvent.class, event -> {
            Player winner = event.getWinner();

            Scheduler scheduler = MinecraftServer.getSchedulerManager();
            scheduler.submitTask(() -> {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> {
                    player.sendMessage(Component.text(winner.getUsername() + " won!", NamedTextColor.RED));
                });

                return TaskSchedule.seconds(1);
            });
        });
    }
}
