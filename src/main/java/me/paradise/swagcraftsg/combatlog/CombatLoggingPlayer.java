package me.paradise.swagcraftsg.combatlog;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
public class CombatLoggingPlayer {
    @Getter
    private String username;

    @Getter
    private UUID uuid;
}
