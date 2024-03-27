package me.paradise.swagcraftsg.map.loader.polar;

import me.paradise.swagcraftsg.map.loader.MapLoader;
import me.paradise.swagcraftsg.map.loader.dummy.NoopChunkLoader;
import net.hollowcube.polar.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class PolarConverter implements MapLoader {
    public static PolarWorld polariseAnvil(Path path) {
        Path polarisedPath = path.resolveSibling(path.getFileName() + ".polar");

        if(Files.exists(polarisedPath)) {
            try {
                byte[] polarisedBytes = Files.readAllBytes(polarisedPath);
                return PolarReader.read(polarisedBytes);
            } catch (IOException e) {
                System.out.println("[ERROR] Failed to read polarised world from disk, regenerating...");
            }
        }

        PolarWorld polarised = null;
        try {
            polarised = AnvilPolar.anvilToPolar(path);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to convert Anvil world to Polar world");
            e.printStackTrace();
            return null;
        }

        byte[] polarWorldBytes = PolarWriter.write(polarised);
        try {
            Files.write(polarisedPath, polarWorldBytes);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to write polarised world to disk");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return polarised;
    }

    public static Instance createInstanceFromPolar(PolarWorld polarWorld) {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        DimensionType fullbright = DimensionType.builder(NamespaceID.from("swagcraft:void"))
                .natural(true) // fix compass rotation
                .ambientLight(2.0f)
                .build();

        MinecraftServer.getDimensionTypeManager().addDimension(fullbright);

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(fullbright);
        PolarLoader loader = new PolarLoader(polarWorld);
        instanceContainer.setChunkLoader(loader);

        loader.loadInstance(instanceContainer);
        var loadingChunks = new ArrayList<CompletableFuture<Chunk>>();
        loader.world().chunks().forEach(chunk -> {
            loadingChunks.add(instanceContainer.loadChunk(chunk.x(), chunk.z()));
        });

        CompletableFuture.allOf(loadingChunks.toArray(CompletableFuture[]::new)).join();

        instanceContainer.setChunkLoader(NoopChunkLoader.INSTANCE);

        return instanceContainer;
    }

    @Override
    public Instance getMapInstance(Path path) {
        return createInstanceFromPolar(polariseAnvil(path));
    }
}
