package me.paradise.swagcraftsg.events;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

public class GameWinEvent implements Event {
    @Getter
    Player winner;

    public GameWinEvent(Player winner) {
        this.winner = winner;
    }
}
