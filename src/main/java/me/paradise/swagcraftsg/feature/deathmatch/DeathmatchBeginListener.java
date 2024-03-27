package me.paradise.swagcraftsg.feature.deathmatch;

import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.chest.ChestManager;
import me.paradise.swagcraftsg.events.DeathmatchBeginEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;

public class DeathmatchBeginListener {
    public DeathmatchBeginListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(DeathmatchBeginEvent.class, event -> {
            // Reset chest loot
            resetChests();

            // Teleport players to the deathmatch arena
            for(Player player: MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if(player.getGameMode().equals(GameMode.SURVIVAL)) {
                    SwagCraftSG.MAP_MANAGER.spawnPlayer(player);
                }
            }
        });
    }


    private void resetChests() {
        ChestManager.getInstance().resetLoottable();
    }
}
