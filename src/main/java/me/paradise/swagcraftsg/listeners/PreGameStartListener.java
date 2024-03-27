package me.paradise.swagcraftsg.listeners;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PreGameStartListener {
    private Match match;
    public PreGameStartListener(Match match) {
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

        // dont break blocks if not ingame
        node.addListener(PlayerBlockBreakEvent.class, event -> {
            if(Match.getGamePhase().equals(GamePhase.WAITING)) {
                event.setCancelled(true);
            }
        });

        node.addListener(PlayerDisconnectEvent.class, event -> {
            SwagCraftSG.MAP_MANAGER.clearSpawnPoint(event.getPlayer());
        });

        MinecraftServer.getGlobalEventHandler().addChild(node);
    }
}
