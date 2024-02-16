package me.paradise.swagcraftsg.menus.items;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.menus.MenuItem;
import me.paradise.swagcraftsg.menus.MenuItemHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class God implements MenuItem {
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.GOD;
    }

    @Override
    public ItemStack getItem() {
        MenuItemHolder holder = this.getMiniMessage();
        // set TextDecoration to false for each holder.getLore() element
        List<Component> lore = new ArrayList<>();
        for(Component component : holder.getLore()) {
            lore.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(Material.DIAMOND_HELMET).displayName(holder.getName().decoration(TextDecoration.ITALIC, false)).lore(lore).meta(mb -> {
            mb.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            mb.enchantment(Enchantment.PROTECTION, (short) 1);
            mb.enchantment(Enchantment.FIRE_PROTECTION, (short) 1);
            mb.enchantment(Enchantment.BLAST_PROTECTION, (short) 1);
            mb.enchantment(Enchantment.PROJECTILE_PROTECTION, (short) 1);
            mb.enchantment(Enchantment.RESPIRATION, (short) 4);
            mb.enchantment(Enchantment.AQUA_AFFINITY, (short) 3);
            mb.enchantment(Enchantment.THORNS, (short) 1);
        }).build();
    }

    private MenuItemHolder getMiniMessage() {
        MiniMessage mm = MiniMessage.miniMessage();
        Component name = mm.deserialize("<bold><aqua>God</aqua></bold>");
        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<blue>GG.</blue>"));
        return new MenuItemHolder(name, lore);
    }
}
