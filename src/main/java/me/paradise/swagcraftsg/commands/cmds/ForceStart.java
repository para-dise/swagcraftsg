package me.paradise.swagcraftsg.commands.cmds;

import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import me.paradise.swagcraftsg.match.MatchInvincibilityTimer;
import me.paradise.swagcraftsg.match.MatchStarter;
import net.minestom.server.command.builder.Command;

public class ForceStart extends Command {
    private MatchStarter matchStarter;

    public ForceStart(MatchStarter matchStarter) {
        super("forcestart");
        this.matchStarter = matchStarter;

        setDefaultExecutor((sender, context) -> {
            if(Match.getGamePhase().equals(GamePhase.INVINCIBILITY)) {
                MatchInvincibilityTimer.REMAINING_INVINCIBILITY_TIME = 6;
                return;
            }

            if(Match.getGamePhase() != GamePhase.WAITING) {
                sender.sendMessage("The match is already in progress!");
                return;
            }

            sender.sendMessage("Force starting the match...");
            matchStarter.setTime(5);
        });
    }
}
