package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class BarbarianImpl extends BasePlayableKit {
    public BarbarianImpl() {
        this.items.add(ItemStack.of(Material.STONE_SWORD));
        this.items.add(ItemStack.of(Material.BREAD, (byte) 3));

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.BARBARIAN;
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
