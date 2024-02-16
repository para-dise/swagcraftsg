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

public class Batman implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.BATMAN;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.LEATHER_HELMET).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><gray>B</gray><yellow>a</yellow><gray>t</gray><yellow>m</yellow><gray>a</gray><yellow>n</yellow></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>It's not who I am underneath,</blue>"));
        lore.add(mm.deserialize("<blue>but what I do that defines</blue>"));
        lore.add(mm.deserialize("<blue>me.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Shift to turn into a bat!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Reduced damage on incoming</blue>"));
        lore.add(mm.deserialize("<blue>arrows.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>When the game starts you</blue>"));
        lore.add(mm.deserialize("<blue>are invisible until invincibility</blue>"));
        lore.add(mm.deserialize("<blue>runs out.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Attack someone to freeze</blue>"));
        lore.add(mm.deserialize("<blue>their hands, when frozen</blue>"));
        lore.add(mm.deserialize("<blue>their are unable to switch</blue>"));
        lore.add(mm.deserialize("<blue>items.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
