package me.paradise.swagcraftsg.menus;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.item.ItemStack;

public interface MenuItem {
    SwagCraftKit getKit();
    ItemStack getItem();
}
