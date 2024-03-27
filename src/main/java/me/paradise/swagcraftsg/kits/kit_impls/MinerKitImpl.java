package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class MinerKitImpl extends BasePlayableKit {

    public MinerKitImpl() {
        this.items.add(ItemStack.builder(Material.STONE_PICKAXE).meta(meta -> {
            meta.enchantment(Enchantment.EFFICIENCY, (short) 0);
        }).build());

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.MINER;
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

    @Override
    public void giveInventory(Player player) {
        super.giveInventory(player);

        player.getInventory().setHelmet(ItemStack.of(Material.IRON_HELMET));
    }
}
