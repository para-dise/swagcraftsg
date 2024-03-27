package me.paradise.swagcraftsg.map;

import com.google.gson.Gson;
import lombok.Getter;
import me.paradise.swagcraftsg.map.gson.MapData;
import me.paradise.swagcraftsg.map.loader.MapLoader;
import me.paradise.swagcraftsg.map.loader.anvil.LegacyAnvilLoader;
import me.paradise.swagcraftsg.map.loader.polar.PolarConverter;
import me.paradise.swagcraftsg.utils.DownloadUtil;
import me.paradise.swagcraftsg.utils.LookUtil;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SwagCraftMap {

    private MapData gameMapData;
    private MapData.Game chosenGameMap;
    private HashMap<UUID, Pos> spawnPoints = new HashMap<>();
    private List<MapData.SpawnPoint> rawSpawnPoints;
    private @Getter Vec center = null;
    private final static MapLoader DEFAULT_LOADER = new PolarConverter(); // Either PolarConverter or LegacyAnvilLoader
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

    public Instance getMapInstance() {
        // Choose random map
        MapData.Game game = gameMapData.getGames().get((int) (Math.random() * gameMapData.getGames().size()));
        this.chosenGameMap = game;
        this.rawSpawnPoints = game.getSpawnPoints();

        // Set map center
        this.center = new Vec(game.getCenter().getX(), game.getCenter().getY(), game.getCenter().getZ());

        // Unzip map
        Path map = Path.of("maps", game.getName() + ".zip");
        Path mapFolder = Path.of("maps", game.getName());
        if(!mapFolder.toFile().exists()) {
            System.out.println("[Map] Unzipping map " + game.getName());
            DownloadUtil.unzip(map.toString(), mapFolder.toString());
        }

        // Return created instance
        Path mapPath = Path.of("maps", game.getName());
        return DEFAULT_LOADER.getMapInstance(mapPath);
    }

    public Pos getSpawnPoint(UUID uuid) {
        if(this.spawnPoints.containsKey(uuid)) { // Cache spawn points
            return this.spawnPoints.get(uuid);
        }

        if(rawSpawnPoints.isEmpty()) {
            System.out.println("[WARN] No more spawn points available, reusing spawn points");
            return this.spawnPoints.entrySet().iterator().next().getValue();
        }

        MapData.SpawnPoint spawnPoint = rawSpawnPoints.get((int) (Math.random() * chosenGameMap.getSpawnPoints().size()));
        Pos playerSpawnPoint = new Pos(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        this.spawnPoints.put(uuid, playerSpawnPoint);

        rawSpawnPoints.remove(spawnPoint);

        return playerSpawnPoint;
    }

    public void clearSpawnPoint(Player player) {
        Pos spawnPoint = this.spawnPoints.get(player.getUuid());
        // add back to pool
        MapData.SpawnPoint spawn = new MapData.SpawnPoint();
        spawn.setX(spawnPoint.x());
        spawn.setY(spawnPoint.y());
        spawn.setZ(spawnPoint.z());

        this.rawSpawnPoints.add(spawn);
    }

    public void spawnPlayer(Player player) {
        player.teleport(getSpawnPoint(player.getUuid()));

        Vec center = this.getCenter();
        player.teleport(LookUtil.lookAt(player.getPosition(), new Pos(center.x(), center.y(), center.z())));
    }
}
