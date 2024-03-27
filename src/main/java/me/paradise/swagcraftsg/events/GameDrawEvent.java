package me.paradise.swagcraftsg.events;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

import java.util.ArrayList;
import java.util.List;

public class GameDrawEvent implements Event {
    @Getter
    private final List<Player> remainingPlayers;

    public GameDrawEvent(List<Player> remainingPlayers) {
        this.remainingPlayers = remainingPlayers;
    }
}
