package me.paradise.swagcraftsg.map.loader.anvil;

import me.paradise.swagcraftsg.map.loader.MapLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;

import java.nio.file.Path;

public class LegacyAnvilLoader implements MapLoader {

    @Override
    public Instance getMapInstance(Path path) {
        DimensionType fullbright = DimensionType.builder(NamespaceID.from("swagcraft:void"))
                .natural(true) // fix compass rotation
                .ambientLight(2.0f)
                .build();

        MinecraftServer.getDimensionTypeManager().addDimension(fullbright);

        return MinecraftServer.getInstanceManager().createInstanceContainer(
                fullbright,
                new AnvilLoader(
                        path
                )
        );
    }
}
