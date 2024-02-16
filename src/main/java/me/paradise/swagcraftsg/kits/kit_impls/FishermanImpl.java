package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class FishermanImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public FishermanImpl() {
        this.items.add(ItemStack.of(Material.FISHING_ROD));
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
    public void giveInventory(Player player) {
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    @Override
    public void registerGlobalListeners() {

    }
}
