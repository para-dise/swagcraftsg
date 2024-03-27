package me.paradise.swagcraftsg.chest.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LootData {
    @SerializedName("loot")
    private Map<Integer, List<String>> loot;

    @SerializedName("multiple")
    private ArrayList<String> multiple;

    public Map<Integer, List<String>> getLoot() {
        return loot;
    }
    public ArrayList<String> getMultiple() {
        return multiple;
    }

    public boolean hasMultiple(String namespacedKey) {
        return multiple.contains(namespacedKey);
    }
}
