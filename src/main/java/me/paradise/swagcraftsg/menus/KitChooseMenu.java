package me.paradise.swagcraftsg.menus;

import lombok.Getter;
import lombok.Setter;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.menus.items.*;
import me.paradise.swagcraftsg.utils.KitNameUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import java.util.LinkedHashSet;

public class KitChooseMenu {
    @Getter @Setter
    Inventory inventory;
    private final LinkedHashSet<MenuItem> items = new LinkedHashSet<MenuItem>() {
        {
            add(new Default());
            add(new Trapper());
            add(new Fisherman());
            add(new Miner());
            add(new Barbarian());
            add(new Creeper());
            add(new Salamander());
            add(new Butcher());
            add(new Flower());
            add(new Ghost());
            add(new Miser());
            add(new Batman());
            add(new Ironman());
            add(new Superman());
            add(new Hulk());
            add(new God());
            add(new SpiderMan());
            add(new Archer());
            add(new Scrapper());
            add(new Pyro());
            add(new Brute());
            add(new Thor());
            add(new Zombie());
            add(new Alchemist());
            add(new Mage());
            add(new SCArcher());
            add(new Assassin());
        }
    };

    public KitChooseMenu() {
        this.inventory = new Inventory(InventoryType.CHEST_3_ROW, Component.text("Choose a kit", NamedTextColor.LIGHT_PURPLE));
        this.init();
        this.inventory.addInventoryCondition((player, slot, clickType, inventoryConditionResult) -> {
            ItemStack clickedItem = inventory.getItemStack(slot);
            if (clickedItem.getMaterial().equals(ItemStack.AIR.getMaterial())) {
                inventoryConditionResult.setCancel(true);
                return;
            }

            // Give player the kit
            KitChooser kitChooser = KitChooser.getInstance();
            boolean success = kitChooser.chooseKit(player, KitNameUtil.itemToKit(clickedItem));
            if(success) {
                player.sendMessage(Component.text("Now using kit ", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false).append(clickedItem.getDisplayName())); // TODO: Add !
                player.closeInventory();
                player.setDisplayName(clickedItem.getDisplayName().append(Component.text(" ", NamedTextColor.WHITE)).append(Component.text(player.getUsername(), NamedTextColor.RED).decoration(TextDecoration.BOLD, false)));
            } else {
                player.sendMessage(Component.text("You can't use that kit.", NamedTextColor.RED));
            }
            inventoryConditionResult.setCancel(true);

        });
    }

    private void init() {
        for(MenuItem item : items) {
            this.inventory.addItemStack(item.getItem());
        }
    }
}
