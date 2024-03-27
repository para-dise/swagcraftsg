package me.paradise.swagcraftsg.combatlog;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CombatLogManager {
    private List<CombatLogger> combatLoggers = new ArrayList<>();

    private void clearOldLogs() {
        List<CombatLogger> toRemove = new ArrayList<>();
        for(CombatLogger logger : combatLoggers) {
            if(logger.getCombatStarted().getTime() + 10000 < new Date().getTime()) {
                toRemove.add(logger);
            }
        }
        combatLoggers.removeAll(toRemove);
    }

    public void addLog(Player combatant1, Player combatant2) {
        this.clearOldLogs();
        this.combatLoggers.add(new CombatLogger(combatant1, combatant2));
    }

    public boolean isInCombat(Player player) {
        this.clearOldLogs();

        for(CombatLogger logger : this.combatLoggers) {
            if(logger.getCombatant1().getUuid().equals(player.getUuid()) || logger.getCombatant2().getUuid().equals(player.getUuid())) {
                return true;
            }
        }
        return false;
    }

    public Player getCombatant(Player player) {
        for(CombatLogger logger : this.combatLoggers) {
            if(logger.getCombatant1().getUuid().equals(player.getUuid())) {
                return MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(logger.getCombatant2().getUuid());
            } else if(logger.getCombatant2().getUuid().equals(player.getUuid())) {
                return MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(logger.getCombatant1().getUuid());
            }
        }
        return null;
    }

}
