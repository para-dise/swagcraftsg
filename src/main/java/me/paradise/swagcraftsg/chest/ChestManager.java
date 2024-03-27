package me.paradise.swagcraftsg.chest;

import com.google.gson.Gson;
import lombok.Getter;
import me.paradise.swagcraftsg.chest.gson.LootData;
import me.paradise.swagcraftsg.utils.PotionUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChestManager {
    @Getter
    private static final ChestManager instance = new ChestManager();

    private HashMap<Point, Inventory> chests = new HashMap<>();
    private LootData lootData;

    public ChestManager() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("loot.json");
        lootData = null;
        try {
            String jsonString = IOUtils.toString(is, StandardCharsets.UTF_8);
            // Convert the string into a JSON object
            Gson gson = new Gson();
            lootData = gson.fromJson(jsonString, LootData.class);
        } catch (IOException e) {
            System.out.println("Failed to convert loot.json to JSON");
            throw new RuntimeException(e);
        }

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(InventoryCloseEvent.class, event -> {
            final Player player = event.getPlayer();
            Inventory inventory = event.getInventory();
            if (inventory != null && inventory.getTitle().equals("Chest")) {
                Sound openChestSound = Sound.sound(Key.key("block.chest.close"), Sound.Source.BLOCK, 5f, 2f);
                player.playSound(openChestSound);
            }
        });

    }

    public void openChest(Point chestPoint, Player player) {
        if(!player.getGameMode().equals(GameMode.SURVIVAL)) {
            return;
        }
        Sound openChestSound = Sound.sound(Key.key("block.chest.open"), Sound.Source.BLOCK, 5f, 1f);
        player.playSound(openChestSound);

        if(chests.containsKey(chestPoint)) {
            Inventory chest = chests.get(chestPoint);
            player.openInventory(chest);
        } else {
            Inventory chest = populateChest();
            chests.put(chestPoint, chest);

            player.openInventory(chest);
        }
    }

    private Inventory populateChest() {
        Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW, "Chest");

        // Determine the number of items in the chest (between 1 to 5)
        Random rand = new Random();
        int numItems = rand.nextInt(5) + 1;

        // Populate the chest with the determined number of items
        List<Integer> slots = chooseUniqueNumbers(numItems);
        for (int i = 0; i < numItems; i++) {
            int selectedTier = weightedRandomTier();
            List<String> lootItems = lootData.getLoot().get(selectedTier);

            // Select a random loot item from the selected tier
            String selectedLootItem = lootItems.get(rand.nextInt(lootItems.size()));

            ItemStack chosenItem = ItemStack.of(Material.fromNamespaceId(selectedLootItem), 1);
            if(chosenItem.getMaterial().equals(Material.SPLASH_POTION)) {
                chosenItem = PotionUtil.getRandomPotion();
            }

            if(!lootData.hasMultiple(selectedLootItem)) {
                inventory.setItemStack(slots.get(i), chosenItem);
                continue;
            }

            // select a random number between 1 - 5
            int stackSize = rand.nextInt(5) + 1;
            inventory.setItemStack(slots.get(i), chosenItem.withAmount(stackSize));
        }

        return inventory;
    }

    private int weightedRandomTier() {
        Random rand = new Random();
        int randValue = rand.nextInt(100) + 1;

        if (randValue <= 20) {
            return 1;
        } else if (randValue <= 40) {
            return 2;
        } else if (randValue <= 70) {
            return 3;
        } else if (randValue <= 90) {
            return 4;
        } else {
            return 5;
        }
    }

    public static List<Integer> chooseUniqueNumbers(int n) {
        if (n > 26) {
            throw new IllegalArgumentException("Cannot choose more unique numbers than available in the range (0-27)");
        }

        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        return numbers.subList(0, n);
    }

    public void resetLoottable() {
        chests.clear();
    }
}
