package me.paradise.swagcraftsg.listeners;

import me.paradise.swagcraftsg.chest.ChestManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.block.Block;

public class PlayerUseChestListener {
    private final ChestManager chestManager = new ChestManager();

    public PlayerUseChestListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            if(event.getBlock().compare(Block.CHEST)) {
                chestManager.openChest(event.getBlockPosition(), event.getPlayer());
            }
        });
    }
}
