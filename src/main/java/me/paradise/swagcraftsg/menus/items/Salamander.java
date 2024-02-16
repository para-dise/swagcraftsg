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

public class Salamander implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.SALAMANDER;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.LAVA_BUCKET).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<gray>Salamander</gray>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>The salamander kit is</blue>"));
        lore.add(mm.deserialize("<blue>a very nice kit for those</blue>"));
        lore.add(mm.deserialize("<blue>who just cannot stop burning!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>You no longer must fear</blue>"));
        lore.add(mm.deserialize("<blue>fire!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Swim in lava!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Bath in flames!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>However, you do seem to be</blue>"));
        lore.add(mm.deserialize("<blue>allergic to water...</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
