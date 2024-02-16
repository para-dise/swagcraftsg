package me.paradise.swagcraftsg.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class ConsumeUtil {
    public static void consume(Player player, ItemStack itemStack) {
        for(int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItemStack(i);
            if(item.isSimilar(itemStack)) {
                player.getInventory().setItemStack(i, item.consume(1));
                break;
            }
        }
    }
}
