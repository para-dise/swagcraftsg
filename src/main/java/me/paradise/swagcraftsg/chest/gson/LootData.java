package me.paradise.swagcraftsg.chest.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class LootData {
    @SerializedName("loot")
    private Map<Integer, List<String>> loot;

    public Map<Integer, List<String>> getLoot() {
        return loot;
    }
}
