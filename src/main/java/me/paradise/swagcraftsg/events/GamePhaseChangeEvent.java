package me.paradise.swagcraftsg.events;

import lombok.Getter;
import me.paradise.swagcraftsg.match.GamePhase;
import net.minestom.server.event.Event;

public class GamePhaseChangeEvent implements Event {
    @Getter
    GamePhase oldState;
    @Getter GamePhase newState;

    public GamePhaseChangeEvent(GamePhase oldState, GamePhase newState) {
        this.oldState = oldState;
        this.newState = newState;
    }
}
