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

public class Creeper implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.CREEPER;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.GUNPOWDER).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<gray>Creeper</gray>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>Explode with the majestic</blue>"));
        lore.add(mm.deserialize("<blue>power of a creeper upon</blue>"));
        lore.add(mm.deserialize("<blue>death!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Wreck havoc upon thy enemies</blue>"));
        lore.add(mm.deserialize("<blue>for daring to kill one</blue>"));
        lore.add(mm.deserialize("<blue>as powerful as you!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Ruin their day as you</blue>"));
        lore.add(mm.deserialize("<blue>destroy any hope and chance</blue>"));
        lore.add(mm.deserialize("<blue>they had of winning!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>However!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Theres a chance you will</blue>"));
        lore.add(mm.deserialize("<blue>barely explode.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
