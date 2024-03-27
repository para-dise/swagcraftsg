package me.paradise.swagcraftsg.combatlog;

import lombok.Getter;
import net.minestom.server.entity.Player;

import java.util.Date;

public class CombatLogger {

    @Getter
    CombatLoggingPlayer combatant1;

    @Getter
    CombatLoggingPlayer combatant2;

    @Getter
    Date combatStarted;

    public CombatLogger(Player combatant1, Player combatant2) {
        this.combatant1 = CombatLoggingPlayer.builder().username(combatant1.getUsername()).uuid(combatant1.getUuid()).build();
        this.combatant2 = CombatLoggingPlayer.builder().username(combatant2.getUsername()).uuid(combatant2.getUuid()).build();
        this.combatStarted = new Date();
    }

}
