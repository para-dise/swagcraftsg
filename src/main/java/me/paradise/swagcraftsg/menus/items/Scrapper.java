package me.paradise.swagcraftsg.menus.items;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.menus.MenuItem;
import me.paradise.swagcraftsg.menus.MenuItemHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class Scrapper implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.SCRAPPER;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.DIAMOND_BOOTS).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> {
            mb.enchantment(Enchantment.FEATHER_FALLING, (short) 1);
            mb.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
        }).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><gold>Scrapper</gold></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>Zoom around with Speed</blue>"));
        lore.add(mm.deserialize("<blue>III!</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
