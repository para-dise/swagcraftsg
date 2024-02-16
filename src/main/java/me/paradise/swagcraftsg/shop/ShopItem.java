package me.paradise.swagcraftsg.shop;

import lombok.Builder;
import lombok.Getter;
import net.minestom.server.item.ItemStack;

@Builder
public class ShopItem {
    @Getter private ItemStack itemStack;
    @Getter private int price;
}
