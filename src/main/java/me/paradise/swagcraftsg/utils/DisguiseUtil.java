package me.paradise.swagcraftsg.utils;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import me.paradise.swagcraftsg.entities.BatmanBat;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

import java.util.HashMap;
import java.util.UUID;

public class DisguiseUtil {
    private HashMap<UUID, EntityCreature> disguiseMap = new HashMap<>();

    public void disguisePlayer(Player player, EntityType entityType) {
        Potion invisibility = new Potion(PotionEffect.INVISIBILITY, (byte) 1, 10000);
        player.addEffect(invisibility);

        spawnPlayerMarker(entityType, player);
    }

    private void spawnPlayerMarker(EntityType entityType, Player trackedPlayer) {
        if(entityType.equals(EntityType.BAT)) {
            EntityCreature entity = new BatmanBat(trackedPlayer);
            entity.setInstance(trackedPlayer.getInstance(), trackedPlayer.getPosition().add(0, 0.5, 0));
            entity.setBoundingBox(trackedPlayer.getBoundingBox());
            entity.setHealth(999999999);
            entity.spawn();

            EventNode<Event> disguised = EventNode.all(trackedPlayer.getUsername() + "-disguise");
            disguised.addListener(PlayerMoveEvent.class, event -> {
                if(entity.isRemoved() || entity.getInstance() == null) {
                    return;
                }
                entity.teleport(event.getNewPosition().add(0, 1.5, 0));
            });

            disguised.addListener(FinalAttackEvent.class, event -> {
                if(event.getTarget() instanceof BatmanBat) {
                    BatmanBat batmanBat = (BatmanBat) event.getTarget();
                    if(batmanBat.getEntityId() == entity.getEntityId()) {
                        trackedPlayer.damage(Damage.fromPlayer((Player) event.getEntity(), event.getBaseDamage()));
                        entity.damage(DamageType.GENERIC, 0);
                        event.setCancelled(true);
                    }
                }
            });

            MinecraftServer.getGlobalEventHandler().addChild(disguised);
            this.disguiseMap.put(trackedPlayer.getUuid(), entity);
        }
    }

    public void undisguisePlayer(Player player) {
        EntityCreature entity = this.disguiseMap.get(player.getUuid());
        if(entity != null) {
            entity.remove();
            this.disguiseMap.remove(player.getUuid());
            player.removeEffect(PotionEffect.INVISIBILITY);
        }
    }
}
