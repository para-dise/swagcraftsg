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

import java.util.Set;

public class KitChooseMenu {
    @Getter @Setter
    Inventory inventory;
    private final Set<MenuItem> items = Set.of(
            new Default(),
            new Trapper(),
            new Fisherman(),
            new Miner(),
            new Barbarian(),
            new Creeper(),
            new Salamander(),
            new Butcher(),
            new Flower(),
            new Ghost(),
            new Miser(),
            new Batman(),
            new Ironman(),
            new Superman(),
            new Hulk(),
            new God(),
            new SpiderMan(),
            new Archer(),
            new Scrapper(),
            new Pyro(),
            new Brute(),
            new Thor(),
            new Zombie(),
            new Alchemist(),
            new Mage(),
            new SCArcher(),
            new Assassin()
    );

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
                player.setDisplayName(clickedItem.getDisplayName().append(Component.text(" ", NamedTextColor.WHITE)).append(Component.text(player.getUsername(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
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
