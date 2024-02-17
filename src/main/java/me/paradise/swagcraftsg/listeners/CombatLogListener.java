package me.paradise.swagcraftsg.listeners;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import lombok.Getter;
import me.paradise.swagcraftsg.combatlog.CombatLogManager;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.network.packet.server.play.SoundEffectPacket;
import net.minestom.server.sound.SoundEvent;

import java.util.List;

public class CombatLogListener {
    @Getter
    CombatLogManager combatLogManager = new CombatLogManager();
    private final static List<Long> HIT_SOUND_SEEDS = List.of(4769332479410481736L, 8946315698180586284L, 3236002118904976264L);

    public void register() {
        EventNode<Event> combatLogNode = EventNode.all("combatlog");
        combatLogNode.addListener(FinalAttackEvent.class, event -> {

            if(!(event.getTarget() instanceof Player) || !(event.getEntity() instanceof Player)) return;

            Player target = (Player) event.getTarget();
            Player attacker = (Player) event.getEntity();

            // Play hit sound as MinestomPvP does not
            float pitch = (float) (Math.random() - Math.random()) * 0.2f + 1.0f;
            long seed = HIT_SOUND_SEEDS.get((int) (Math.random() * HIT_SOUND_SEEDS.size()));

            target.sendPacketToViewersAndSelf(new SoundEffectPacket(
                    SoundEvent.ENTITY_PLAYER_HURT,
                    null,
                    null,
                    Sound.Source.PLAYER,
                    target.getPosition().blockX(),
                    target.getPosition().blockY(),
                    target.getPosition().blockZ(),
                    1.0f,
                    pitch,
                    seed
            ));

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
