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

public class Assassin implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.ASSASSIN;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.CHAINMAIL_HELMET).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><blue>A</blue><light_purple>s</light_purple><blue>s</blue><light_purple>a</light_purple><blue>s</blue><light_purple>s</light_purple><blue>i</blue><light_purple>n</light_purple></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>Speed II.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Jump I.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Confuse players using</blue>"));
        lore.add(mm.deserialize("<blue>snowballs.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Headshot = Insta Kill.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Eat an apple for invisibility.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Eat a potato to see at</blue>"));
        lore.add(mm.deserialize("<blue>night.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
