package me.paradise.swagcraftsg.shop;

import com.google.gson.Gson;
import lombok.Getter;
import me.paradise.swagcraftsg.chest.gson.LootData;
import me.paradise.swagcraftsg.shop.gson.ShopData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.TransactionOption;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shop {
    private HashMap<Integer, ArrayList<ShopItem>> items = new HashMap<>();
    @Getter private Inventory inventory;

    public Shop() {
        // Read JSON
        ShopData shopData = getShopData();
        for(Integer key : shopData.getData().keySet()) { // row
            ArrayList<ShopItem> itemArrayList = new ArrayList<>();
            for(ShopData.Item item : shopData.getData().get(key)) { // item
                System.out.println("[ShopData] Loading item " + item.getMaterial() + " with price " + item.getPrice());
                Material material = Material.fromNamespaceId(item.getMaterial());
                if(material == null) {
                    System.out.println("[ShopData] Material " + item.getMaterial() + " does not exist!");
                    continue;
                }
                ItemStack itemStack = ItemStack.of(Material.fromNamespaceId(item.getMaterial()), 1).withTag(Tag.Integer("price"), item.getPrice()).withAmount(item.getAmount());
                itemArrayList.add(ShopItem.builder().itemStack(itemStack).price(item.getPrice()).build());
            }
            items.put(key, itemArrayList);
        }

        Inventory initInventory = new Inventory(InventoryType.CHEST_5_ROW, Component.text("Shop", NamedTextColor.BLUE));
        List<ItemStack> itemStacksList = new ArrayList<>();
        for(Integer key : items.keySet()) {
            for(ShopItem item : items.get(key)) {
                itemStacksList.add(item.getItemStack());
            }
        }

        TransactionOption<ItemStack> option = TransactionOption.ALL;
        initInventory.addItemStacks(itemStacksList, option);

        initInventory.addInventoryCondition((player, slot, clickType, inventoryConditionResult) -> {
            if(slot > 44 || slot < 0) {
                return;
            }

            ItemStack itemStack = initInventory.getItemStack(slot);
            Integer price = itemStack.getTag(Tag.Integer("price"));

            if(price == null) {
                player.sendMessage(Component.text("This item does not have a price tag!", NamedTextColor.RED));
                return;
            }


            if(player.getInventory().addItemStack(itemStack.withAmount(1))) {
                // todo: clone with amount 1 and use cursor if pr changes logic
                player.sendMessage(Component.text("Item purchased for $" + price, NamedTextColor.RED));
            } else {
                player.sendMessage(Component.text("You do not have enough space in your inventory!", NamedTextColor.RED));
            }

            inventoryConditionResult.setCancel(true);
        });

        this.inventory = initInventory;
    }

    private ShopData getShopData() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("shop.json");
        ShopData data = null;
        try {
            String jsonString = IOUtils.toString(is, StandardCharsets.UTF_8);
            // Convert the string into a JSON object
            Gson gson = new Gson();
            data = ShopData.fromJson(jsonString);
        } catch (IOException e) {
            System.out.println("Failed to convert shop.json to JSON");
            throw new RuntimeException(e);
        }
        return data;
    }
}
