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

public class Hulk implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.HULK;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.LEATHER_LEGGINGS).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><green>Hulk</green></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>HULK SMASH!!!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Shift punch the ground</blue>"));
        lore.add(mm.deserialize("<blue>to activate hulk smash!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Incoming damage will slowly</blue>"));
        lore.add(mm.deserialize("<blue>regen food bars.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Jump I and Strength I.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Take less fall damage,</blue>"));
        lore.add(mm.deserialize("<blue>damage you would have</blue>"));
        lore.add(mm.deserialize("<blue>taken is transferred to</blue>"));
        lore.add(mm.deserialize("<blue>nearby players.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
