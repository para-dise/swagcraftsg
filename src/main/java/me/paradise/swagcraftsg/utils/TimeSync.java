package me.paradise.swagcraftsg.utils;

import lombok.Getter;
import lombok.Setter;

public class TimeSync {
    @Getter private static final TimeSync instance = new TimeSync();

    @Getter @Setter
    private boolean started = false;

    @Getter @Setter
    private boolean invincibility = false;

    @Getter @Setter private int time = 0;

    @Getter @Setter
    private boolean deathmatch = false;
}
