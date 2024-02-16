package me.paradise.swagcraftsg.match;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.Set;

public class MatchInvincibilityTimer {

    private int timeLeft = 31;
    private Set<Integer> announceTimes = Set.of(30, 15, 10, 5, 4, 3, 2, 1);
    private Match gameMatch;

    public MatchInvincibilityTimer(Match match) {
        this.gameMatch = match;
    }

    public void startTimer() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            timeLeft--;

            if(announceTimes.contains(timeLeft)) {
                for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    player.sendMessage(Component.text("Invincibility wears off in " + timeLeft + " seconds!", NamedTextColor.RED));
                }
            }

            if(timeLeft == 0) {

                for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    player.sendMessage(Component.text("Invincibility has worn off!", NamedTextColor.RED));
                }
                this.gameMatch.setPhase(GamePhase.INGAME);
                return TaskSchedule.stop();
            }

            return TaskSchedule.seconds(1);
        });
    }

}
