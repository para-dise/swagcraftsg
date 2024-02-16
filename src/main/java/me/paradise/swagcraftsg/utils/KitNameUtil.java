package me.paradise.swagcraftsg.utils;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.item.ItemStack;

public class KitNameUtil {
    public static SwagCraftKit itemToKit(ItemStack itemStack) {
        if(itemStack.getDisplayName() == null) return null;

        String name = PlainTextComponentSerializer.plainText().serialize(itemStack.getDisplayName());
        if(name.contains("-")) {
            name = name.replace("-", "_");
        }
        return SwagCraftKit.valueOf(name.toUpperCase());
    }
}
