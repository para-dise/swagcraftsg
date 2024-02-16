package me.paradise.swagcraftsg.map;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import me.paradise.swagcraftsg.map.gson.MapData;
import me.paradise.swagcraftsg.utils.DownloadUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import org.apache.commons.io.IOUtils;

public class SwagCraftMap {

    private MapData gameMapData;
    private MapData.Game chosenGameMap;
    private HashMap<UUID, Pos> spawnPoints = new HashMap<>();

    public SwagCraftMap() {
        // Load "maps.json" from resources
        InputStream is = getClass().getClassLoader().getResourceAsStream("maps.json");
        // Read input stream into a string
        MapData mapData = null;
        try {
            String jsonString = IOUtils.toString(is, StandardCharsets.UTF_8);
            // Convert the string into a JSON object
            Gson gson = new Gson();
            mapData = gson.fromJson(jsonString, MapData.class);
            this.gameMapData = mapData;
            System.out.println("[Map] Loaded " + mapData.getGames().size() + " games and " + mapData.getLobbies().size() + " lobbies");
        } catch (IOException e) {
            System.out.println("Failed to convert maps.json to JSON");
            throw new RuntimeException(e);
        }


        Path maps = Path.of("maps");
        if(!maps.toFile().exists()) {
            System.out.println("[Map] Creating maps directory");
            if(!maps.toFile().mkdir()) {
                System.out.println("[Map] Failed to create maps directory");
            } else {
                System.out.println("[Map] Created maps directory");
            }
        }

        // Download all unavailable maps
        for(MapData.Game game : mapData.getGames()) {
            Path map = Path.of("maps", game.getName() + ".zip");
            if (!map.toFile().exists()) {
                System.out.println("[Map] Downloading map " + game.getName());
                DownloadUtil.downloadFile(game.getURL(), map.toString());
            }
        }

    }

    public MapData.Game getMapByName(String name) {
        for(MapData.Game game : gameMapData.getGames()) {
            if(game.getName().equalsIgnoreCase(name)) {
                return game;
            }
        }
        return null;
    }

    public MapData.Game getLobbyByName(String name) {
        for(MapData.Game game : gameMapData.getLobbies()) {
            if(game.getName().equalsIgnoreCase(name)) {
                return game;
            }
        }
        return null;
    }

    public Instance getMapInstance() {
        // Choose random map
        MapData.Game game = gameMapData.getGames().get((int) (Math.random() * gameMapData.getGames().size()));
        this.chosenGameMap = game;

        // Unzip map
        Path map = Path.of("maps", game.getName() + ".zip");
        Path mapFolder = Path.of("maps", game.getName());
        if(!mapFolder.toFile().exists()) {
            System.out.println("[Map] Unzipping map " + game.getName());
            DownloadUtil.unzip(map.toString(), mapFolder.toString());
        }

        return MinecraftServer.getInstanceManager().createInstanceContainer(
                new AnvilLoader(
                    "maps/" + game.getName()
                )
        );
    }

    public Pos getSpawnPoint(UUID uuid) {
        if(this.spawnPoints.containsKey(uuid)) { // Cache spawn points
            return this.spawnPoints.get(uuid);
        }

        MapData.SpawnPoint spawnPoint = chosenGameMap.getSpawnPoints().get((int) (Math.random() * chosenGameMap.getSpawnPoints().size()));
        Pos playerSpawnPoint = new Pos(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        this.spawnPoints.put(uuid, playerSpawnPoint);

        return playerSpawnPoint;
    }
}
