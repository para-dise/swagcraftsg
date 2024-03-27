package me.paradise.swagcraftsg.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.network.packet.server.play.HitAnimationPacket;

public class DamageUtil {
    public static void damage(Player player, Damage damage) {
        // send hurt animation
        player.sendPacketToViewersAndSelf(new HitAnimationPacket(
                player.getEntityId(), player.getPosition().yaw()
        ));

        player.damage(damage);
    }
}
