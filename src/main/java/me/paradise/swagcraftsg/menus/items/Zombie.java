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

public class Zombie implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.ZOMBIE;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.ZOMBIE_HEAD).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><dark_green>Zombie</dark_green></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>Shift to disguise as a</blue>"));
        lore.add(mm.deserialize("<blue>zombie!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Mobs don't attack you</blue>"));
        lore.add(mm.deserialize("<blue>until you attack them</blue>"));
        lore.add(mm.deserialize("<blue>first.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Chance to poison players</blue>"));
        lore.add(mm.deserialize("<blue>with every hit.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>You will be healed when</blue>"));
        lore.add(mm.deserialize("<blue>damaging players.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Food level will restore</blue>"));
        lore.add(mm.deserialize("<blue>after each kill.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Fist makes damage like</blue>"));
        lore.add(mm.deserialize("<blue>a stone sword.</blue>"));

        return new MenuItemHolder(name, lore);
    }
}
