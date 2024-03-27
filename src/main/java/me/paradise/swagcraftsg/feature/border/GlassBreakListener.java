package me.paradise.swagcraftsg.feature.border;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;

public class GlassBreakListener {
    public GlassBreakListener() {
        EventNode<Event> glassBreakNode = EventNode.all("glass_break");

        glassBreakNode.addListener(PlayerBlockBreakEvent.class, event -> {
            if(event.getBlock().equals(Block.GLASS)) {
                event.setCancelled(true);
            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(glassBreakNode);
    }
}
