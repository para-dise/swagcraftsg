package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;

public interface SwagCraftPlayableKit {
    SwagCraftKit getKit();
    void applyEffects(Player player);
    void registerListeners(Player player);
    void giveInventory(Player player);
    void registerGlobalListeners();

    default void registerInGameListeners() {
        // Default implementation is empty
    }

    default void applyPregameEffects(Player player) {
        // Default implementation is empty
    }
}
