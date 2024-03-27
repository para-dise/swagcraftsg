package me.paradise.swagcraftsg.shop.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopData {
    private Map<Integer, List<Item>> data;

    public ShopData(Map<Integer, List<Item>> data) {
        this.data = data;
    }

    public Map<Integer, List<Item>> getData() {
        return data;
    }

    // Custom JsonSerializer for ShopData
    private static class ShopDataSerializer implements JsonSerializer<ShopData> {
        @Override
        public JsonElement serialize(ShopData shopData, Type type, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<Integer, List<Item>> entry : shopData.getData().entrySet()) {
                int itemId = entry.getKey();
                List<Item> items = entry.getValue();
                JsonArray itemArray = new JsonArray();
                for (Item item : items) {
                    JsonObject itemObject = new JsonObject();
                    itemObject.addProperty("material", item.getMaterial());
                    itemObject.addProperty("price", item.getPrice());
                    itemArray.add(itemObject);
                }
                jsonObject.add(String.valueOf(itemId), itemArray);
            }
            return jsonObject;
        }
    }

    // Method to serialize ShopData to JSON
    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ShopData.class, new ShopDataSerializer())
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    private static class ShopDataDeserializer implements JsonDeserializer<ShopData> {
        @Override
        public ShopData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Map<Integer, List<Item>> data = new HashMap<>();
            JsonObject jsonObject = json.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                int itemId = Integer.parseInt(entry.getKey());
                List<Item> items = new ArrayList<>();
                JsonArray itemArray = entry.getValue().getAsJsonArray();

                for (JsonElement itemElement : itemArray) {
                    JsonObject itemObject = itemElement.getAsJsonObject();
                    String material = itemObject.get("material").getAsString();
                    int price = itemObject.get("price").getAsInt();
                    int amount = (itemObject.has("amount")) ? itemObject.get("amount").getAsInt() : 1; // Default to 1 if not present
                    Item item = new Item(material, price, amount);
                    items.add(item);
                }

                data.put(itemId, items);
            }

            return new ShopData(data);
        }
    }

    // Method to deserialize JSON to ShopData
    public static ShopData fromJson(String jsonString) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ShopData.class, new ShopDataDeserializer())
                .create();
        return gson.fromJson(jsonString, ShopData.class);
    }
    
    public static class Item {
        private String material;
        private int price;
        private Integer amount;

        public Item(String material, int price, Integer amount) {
            this.material = material;
            this.price = price;
            this.amount = amount;
        }

        public String getMaterial() {
            return material;
        }

        public int getPrice() {
            return price;
        }

        public Integer getAmount() {
            return amount;
        }
    }
}
