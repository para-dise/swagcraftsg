package me.paradise.swagcraftsg.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.HitAnimationPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

public class ExplosionUtil {
    public static void explode(float centerX, float centerY, float centerZ, float strength, Instance instance, Entity damager) {
        //instance.explode(centerX, centerY, centerZ, strength);

        // spawn particle
        Pos pos = new Pos(centerX, centerY + 1, centerZ);
        instance.getNearbyEntities(pos, 60).forEach(entity -> {
            if(entity instanceof Player player) {
                ParticlePacket particlePacket = new ParticlePacket(Particle.EXPLOSION_EMITTER.id(), true, (float) pos.x(), (float) pos.y(), (float) pos.z(), 0, 0, 0, 10, 1, null);
                //ParticlePacket particlePacket = ParticleCreator.createParticlePacket(Particle.EXPLOSION_EMITTER, pos.x(), pos.y(), pos.z(), 0f, 0f, 0f, 1);
                player.sendPacket(particlePacket);

                // play explosion sound
                player.playSound(Sound.sound(Key.key("entity.generic.explode"), Sound.Source.AMBIENT, 1f, 1f));
            }
        });

        // damage entities
        instance.getNearbyEntities(pos, 5).forEach(entity -> {
            if(entity instanceof Player player) {
                player.damage(DamageType.EXPLOSION, 5);

                // send hurt packet
                player.sendPacketToViewersAndSelf(new HitAnimationPacket(
                        player.getEntityId(), player.getPosition().yaw()
                ));

                // play sound
                player.sendPacket(SoundUtil.getHitPacket(player));

                // kb player
                KnockbackUtil.pushAway(player, pos);
            }
        });
    }
}
