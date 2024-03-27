package me.paradise.swagcraftsg.map.loader;

import net.minestom.server.instance.Instance;

import java.nio.file.Path;

public interface MapLoader {
    Instance getMapInstance(Path path);
}
