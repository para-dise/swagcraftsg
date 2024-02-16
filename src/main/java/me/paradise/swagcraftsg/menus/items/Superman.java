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

public class Superman implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.SUPERMAN;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.GOLDEN_CHESTPLATE).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><gold>S</gold><yellow>u</yellow><gold>p</gold><yellow>e</yellow><gold>r</gold><yellow>m</yellow><gold>a</gold><yellow>n</yellow></bold>");
        List<Component> lore = new ArrayList<>();

        lore.add(mm.deserialize("<blue>It's a bird, it's a plane,</blue>"));
        lore.add(mm.deserialize("<blue>no, it's Superman!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Fly for 5 seconds, but</blue>"));
        lore.add(mm.deserialize("<blue>you will take double fall</blue>"));
        lore.add(mm.deserialize("<blue>damage!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Immune to harmful potions.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Reduced damage on incoming</blue>"));
        lore.add(mm.deserialize("<blue>attacks.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Health will regen faster</blue>"));
        lore.add(mm.deserialize("<blue>than normal.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
