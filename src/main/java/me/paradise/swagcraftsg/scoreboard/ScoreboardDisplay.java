package me.paradise.swagcraftsg.scoreboard;

import lombok.Getter;
import me.paradise.swagcraftsg.feature.border.GameBorder;
import me.paradise.swagcraftsg.match.Match;
import me.paradise.swagcraftsg.utils.TimeSync;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreboardDisplay {
    @Getter
    private final Sidebar sidebar = new Sidebar(Component.text("Stage: ", NamedTextColor.DARK_AQUA).append(this.getStage()));
    private Collection<Player> audience;

    public ScoreboardDisplay() {
        // schedule scoreboard updating
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            this.sidebar.setTitle(Component.text("Stage: ", NamedTextColor.DARK_AQUA).append(this.getStage()));
            this.sidebar.removeLine("time");
            this.sidebar.removeLine("server");
            this.sidebar.removeLine("players");

            for(Sidebar.ScoreboardLine line : createLines()) {
                sidebar.createLine(line);
            }

            return TaskSchedule.seconds(1);
        });
    }

    private List<Sidebar.ScoreboardLine> createLines() {
        List<Sidebar.ScoreboardLine> lines = new ArrayList<>();

        if(TimeSync.getInstance().isStarted()) {
            String displayText = TimeSync.getInstance().isInvincibility() ? "Starting in: " : "Time remaining: ";
            lines.add(new Sidebar.ScoreboardLine(
                    "time",
                    Component.text(displayText, NamedTextColor.GOLD),
                    Math.abs(TimeSync.getInstance().getTime())
            ));
        } else {
            lines.add(new Sidebar.ScoreboardLine(
                    "time",
                    Component.text("Starting in: ", NamedTextColor.GOLD),
                    Math.abs(TimeSync.getInstance().getTime())
            ));
        }

        lines.add(new Sidebar.ScoreboardLine(
                "server",
                Component.text("Server: ", NamedTextColor.YELLOW),
                15
        ));

        lines.add(new Sidebar.ScoreboardLine(
                "players",
                Component.text("Players: ", NamedTextColor.GREEN),
                getSurvivalPlayerCount()
                //MinecraftServer.getConnectionManager().getOnlinePlayerCount() // TODO: is this more performant?
        ));

        return lines;
    }

    private Component getStage() {
        if(TimeSync.getInstance().isDeathmatch()) {
            return Component.text("Deathmatch", NamedTextColor.DARK_RED);
        }

        switch (Match.getGamePhase()) {
            case WAITING:
                return Component.text("Pregame", NamedTextColor.AQUA);
            case INVINCIBILITY:
                return Component.text("Invincibility", NamedTextColor.AQUA);
            case INGAME:
                return Component.text("Fighting", NamedTextColor.AQUA);
            default:
                return Component.text("Unknown");
        }
    }

    public void display(Player player) {
        this.sidebar.addViewer(player);
    }

    private int getSurvivalPlayerCount() {
        return (int) MinecraftServer.getConnectionManager().getOnlinePlayers().stream().filter(player -> player.getGameMode().equals(GameMode.SURVIVAL)).count();
    }

}
