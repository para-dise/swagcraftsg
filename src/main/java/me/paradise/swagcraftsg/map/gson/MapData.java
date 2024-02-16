package me.paradise.swagcraftsg.map.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import net.minestom.server.coordinate.Pos;

public class MapData {
    @SerializedName("games")
    private List<Game> games;

    @SerializedName("lobbies")
    private List<Game> lobbies;

    public List<Game> getGames() {
        return games;
    }

    public List<Game> getLobbies() {
        return lobbies;
    }

    public class Game {
        @SerializedName("name")
        private String name;

        @SerializedName("url")
        private String URL;

        @SerializedName("spawnPoints")
        private List<SpawnPoint> spawnPoints;

        public String getName() {
            return name;
        }

        public String getURL() {
            return URL;
        }

        public List<SpawnPoint> getSpawnPoints() {
            return spawnPoints;
        }
    }

    public class SpawnPoint {
        @SerializedName("x")
        private int x;

        @SerializedName("y")
        private int y;

        @SerializedName("z")
        private int z;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }
}