package me.paradise.swagcraftsg.menus.items;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.menus.MenuItem;
import me.paradise.swagcraftsg.menus.MenuItemHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.item.*;

import java.util.ArrayList;
import java.util.List;

public class SpiderMan implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.SPIDER_MAN;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.LEATHER_CHESTPLATE).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> {
            mb.hideFlag(ItemHideFlag.HIDE_ENCHANTS).hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            mb.enchantment(Enchantment.UNBREAKING, (short) 2);
        }).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><red>Spider</red><dark_gray>-</dark_gray><blue>Man</blue></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>With great power comes</blue>"));
        lore.add(mm.deserialize("<blue>great responsibility.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Swing around using a fishing</blue>"));
        lore.add(mm.deserialize("<blue>rod!</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Shoot webs from a bow.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Activate wall climbing</blue>"));
        lore.add(mm.deserialize("<blue>by shifting.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Fall damage is reduced.</blue>"));
        lore.add(Component.empty());
        lore.add(mm.deserialize("<blue>Speed 1.</blue>"));
        return new MenuItemHolder(name, lore);
    }
}
