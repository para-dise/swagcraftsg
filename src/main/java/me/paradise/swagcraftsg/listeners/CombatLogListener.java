package me.paradise.swagcraftsg.listeners;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import lombok.Getter;
import me.paradise.swagcraftsg.combatlog.CombatLogManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class CombatLogListener {
    @Getter
    CombatLogManager combatLogManager = new CombatLogManager();

    public void register() {
        EventNode<Event> combatLogNode = EventNode.all("combatlog");
        combatLogNode.addListener(FinalAttackEvent.class, event -> {

            if(!(event.getTarget() instanceof Player) || !(event.getEntity() instanceof Player)) return;

            Player target = (Player) event.getTarget();
            Player attacker = (Player) event.getEntity();

            combatLogManager.addLog(target, attacker);
        });

        combatLogNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
            if(!(event.getEntity().getShooter() instanceof Player) || !((event.getHitEntity()) instanceof Player)) return;

            Player target = (Player) event.getHitEntity();
            Player attacker = (Player) event.getEntity().getShooter();

            combatLogManager.addLog(target, attacker);
        });
        MinecraftServer.getGlobalEventHandler().addChild(combatLogNode);
    }
}
