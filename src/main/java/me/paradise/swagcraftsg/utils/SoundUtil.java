package me.paradise.swagcraftsg.utils;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Entity;
import net.minestom.server.network.packet.server.play.SoundEffectPacket;
import net.minestom.server.sound.SoundEvent;

import java.util.List;

public class SoundUtil {
    private final static List<Long> HIT_SOUND_SEEDS = List.of(4769332479410481736L, 8946315698180586284L, 3236002118904976264L);

    public static long getHitSoundSeed() {
        return HIT_SOUND_SEEDS.get((int) (Math.random() * HIT_SOUND_SEEDS.size()));
    }

    public static float getHitSoundPitch() {
        return (float) (Math.random() - Math.random()) * 0.2f + 1.0f;
    }

    public static SoundEffectPacket getHitPacket(Entity target) {
        return new SoundEffectPacket(
                SoundEvent.ENTITY_PLAYER_HURT,
                null,
                null,
                Sound.Source.PLAYER,
                target.getPosition().blockX(),
                target.getPosition().blockY(),
                target.getPosition().blockZ(),
                1.0f,
                SoundUtil.getHitSoundPitch(),
                SoundUtil.getHitSoundSeed()
        );
    }
}
