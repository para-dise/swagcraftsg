package me.paradise.swagcraftsg.match.gamemode;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GamemodeManager {

    private HashMap<UUID, GameMode> gamemodes = new HashMap<>();

    public void setGamemode(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public GameMode getGamemode(Player player) {
        return this.gamemodes.get(player.getUuid());
    }

}
