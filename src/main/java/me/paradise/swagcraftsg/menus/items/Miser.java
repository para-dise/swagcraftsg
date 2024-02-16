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

public class Miser implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.MISER;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.CHEST).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<gray>Miser</gray>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>Tired of people killing</blue>"));
        lore.add(mm.deserialize("<blue>you and grabbing your</blue>"));
        lore.add(mm.deserialize("<blue>hard earned loot? Worry</blue>"));
        lore.add(mm.deserialize("<blue>no more!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Revenge from the grave!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>They ain't getting any</blue>"));
        lore.add(mm.deserialize("<blue>of your loot!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>You no longer drop your</blue>"));
        lore.add(mm.deserialize("<blue>items on death!</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
