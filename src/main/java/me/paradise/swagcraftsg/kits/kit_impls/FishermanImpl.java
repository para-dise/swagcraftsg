package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class FishermanImpl extends BasePlayableKit {
    public FishermanImpl() {
        this.items.add(ItemStack.of(Material.FISHING_ROD));

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.FISHERMAN;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void registerGlobalListeners() {

    }
}
