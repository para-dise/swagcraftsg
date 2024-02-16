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
import net.minestom.server.potion.Potion;

import java.util.ArrayList;
import java.util.List;

public class Alchemist implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.ALCHEMIST;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        // create potion of healing
        return ItemStack.builder(Material.POTION).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES).hideFlag(ItemHideFlag.HIDE_POTION_EFFECTS)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><blue>Alchemist</blue></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>Loaded with pots.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Confuse players using</blue>"));
        lore.add(mm.deserialize("<blue>snowballs.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Chance to poison players</blue>"));
        lore.add(mm.deserialize("<blue>with every hit.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
