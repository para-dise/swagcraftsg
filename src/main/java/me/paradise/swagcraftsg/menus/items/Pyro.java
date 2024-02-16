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

public class Pyro implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.PYRO;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.FLINT_AND_STEEL).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><red>Pyro</red></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>Gain strength when in</blue>"));
        lore.add(mm.deserialize("<blue>fire!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>No fire damage.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Pigs always drop 2 porkchops.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
