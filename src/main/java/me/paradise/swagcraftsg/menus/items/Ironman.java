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

public class Ironman implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.IRONMAN;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.IRON_CHESTPLATE).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><red>Iron</red><gold>man</gold></bold>");
        List<Component> lore = new ArrayList<>();

        lore.add(mm.deserialize("<blue>Jarvis, eliminate Notch!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Jump I and Speed I.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Fist makes damage like</blue>"));
        lore.add(mm.deserialize("<blue>an Iron Sword.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Right click on a noteblock</blue>"));
        lore.add(mm.deserialize("<blue>and give everything in</blue>"));
        lore.add(mm.deserialize("<blue>audible range nausea.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Your magnetic Force-Field</blue>"));
        lore.add(mm.deserialize("<blue>scrambles players compasses</blue>"));
        lore.add(mm.deserialize("<blue>when they get within 25</blue>"));
        lore.add(mm.deserialize("<blue>blocks.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
