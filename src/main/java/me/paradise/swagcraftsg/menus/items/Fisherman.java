package me.paradise.swagcraftsg.menus.items;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.menus.MenuItem;
import me.paradise.swagcraftsg.menus.MenuItemHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class Fisherman implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.FISHERMAN;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.FISHING_ROD).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<gray>Fisherman</gray>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>SG 1.0 Kit.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Unfortunately, all information</blue>"));
        lore.add(mm.deserialize("<blue>related to this kit has been</blue>"));
        lore.add(mm.deserialize("<blue>lost in the depths of the ocean.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>As compensation, you get</blue>"));
        lore.add(mm.deserialize("<blue>a fishing rod.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
