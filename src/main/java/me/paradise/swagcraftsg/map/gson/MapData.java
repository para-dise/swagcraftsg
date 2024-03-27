package me.paradise.swagcraftsg.map.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

        @SerializedName("center")
        private SpawnPoint center;

        public String getName() {
            return name;
        }

        public String getURL() {
            return URL;
        }

        public List<SpawnPoint> getSpawnPoints() {
            return spawnPoints;
        }

        public SpawnPoint getCenter() {
            return center;
        }
    }

    public static class SpawnPoint {
        @SerializedName("x")
        private double x;

        @SerializedName("y")
        private double y;

        @SerializedName("z")
        private double z;

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        // Setter methods
        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setZ(double z) {
            this.z = z;
        }
    }
}