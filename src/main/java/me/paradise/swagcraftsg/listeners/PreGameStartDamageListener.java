package me.paradise.swagcraftsg.listeners;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class PreGameStartDamageListener {
    private Match match;
    public PreGameStartDamageListener(Match match) {
        this.match = match;

        EventNode<Event> node = EventNode.all("preStart");
        node.addListener(FinalAttackEvent.class, event -> {
            if(match.getGamePhase() != GamePhase.INGAME) {
                event.setCancelled(true);
            } else {
                MinecraftServer.getGlobalEventHandler().removeChild(node);
            }
        });

        node.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
            if(match.getGamePhase() != GamePhase.INGAME) {
                event.setCancelled(true);
            } else {
                MinecraftServer.getGlobalEventHandler().removeChild(node);
            }
        });
        MinecraftServer.getGlobalEventHandler().addChild(node);
    }
}
