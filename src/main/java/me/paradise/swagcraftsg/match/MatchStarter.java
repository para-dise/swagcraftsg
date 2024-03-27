package me.paradise.swagcraftsg.match;

import lombok.Getter;
import lombok.Setter;
import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.commands.CommandManager;
import me.paradise.swagcraftsg.commands.cmds.ForceStart;
import me.paradise.swagcraftsg.utils.TimeSync;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.Set;

public class MatchStarter {

    @Getter boolean started = false;
    @Setter int time = 61;
    private Set<Integer> displayTimes = Set.of(60, 30, 15, 10, 5, 4, 3, 2, 1);
    private Match gameMatch;

    public MatchStarter(Match match) {
        this.gameMatch = match;
    }

    public void startCountdown() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            time--;

            TimeSync.getInstance().setTime(time);

            if(displayTimes.contains(time)) {
                for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    player.sendMessage(Component.text("The game will start in " + time + " seconds", NamedTextColor.RED));
                }
                System.out.println("The game will start in " + time + " seconds");
            }

            if(time == 0) {

                // Ensure that there are enough players to start the match
                if(!this.canStart()) {
                    for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        player.sendMessage(Component.text("There are not enough players to start the match!", NamedTextColor.RED));
                        System.out.println("There are not enough players to start the match!");
                    }

                    this.time = 61;

                    if(SwagCraftSG.DEBUG) {
                        this.gameMatch.populateLobby(23);
                        this.time = 6;
                    }

                    return TaskSchedule.seconds(1);
                }

                this.startMatch();
                return TaskSchedule.stop();
            }

            return TaskSchedule.seconds(1);
        });

        // Register command to start match
        CommandManager.registerCommand(new ForceStart(this));
    }

    private void startMatch() {
        this.started = true;
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.sendMessage(Component.text("The match has started!", NamedTextColor.RED));
        }
        System.out.println("Match started!");
        this.gameMatch.setPhase(GamePhase.INVINCIBILITY);
    }

    private boolean canStart() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().size() >= 2;
    }

}
