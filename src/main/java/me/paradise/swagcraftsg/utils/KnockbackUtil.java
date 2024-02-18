package me.paradise.swagcraftsg.utils;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;

public class KnockbackUtil {
    public static void pushAway(LivingEntity willBePushed, Pos pushFrom) {
        willBePushed.setVelocity(pushFrom.asVec().sub(willBePushed.getPosition().asVec()).normalize().mul(-1*20));
    }
}
