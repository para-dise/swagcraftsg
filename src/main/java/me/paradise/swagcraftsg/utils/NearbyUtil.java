package me.paradise.swagcraftsg.utils;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NearbyUtil {
    public static List<Player> getNearbyPlayers(Player player, int radius) {
        List<Player> playerList = new ArrayList<>();
        for(Player serverPlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if(serverPlayer.getPosition().distance(player.getPosition()) <= radius && serverPlayer.getUuid() != player.getUuid()) {
                playerList.add(serverPlayer);
            }
        }
        return playerList;
    }
}
