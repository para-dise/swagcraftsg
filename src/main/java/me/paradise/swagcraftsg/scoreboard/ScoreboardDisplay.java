package me.paradise.swagcraftsg.scoreboard;

import lombok.Getter;
import me.paradise.swagcraftsg.match.Match;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
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
        lines.add(new Sidebar.ScoreboardLine(
                "server",
                Component.text("Server: ", NamedTextColor.YELLOW),
                15
        ));

        lines.add(new Sidebar.ScoreboardLine(
                "players",
                Component.text("Players: ", NamedTextColor.GREEN),
                MinecraftServer.getConnectionManager().getOnlinePlayerCount()
        ));

        return lines;
    }

    private Component getStage() {
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

}
