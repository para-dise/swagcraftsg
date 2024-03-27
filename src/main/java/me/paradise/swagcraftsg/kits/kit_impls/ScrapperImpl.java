package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

public class ScrapperImpl extends BasePlayableKit {

    private static final Potion SCRAPPER_SPEED = new Potion(PotionEffect.SPEED, (byte) 2, Potion.INFINITE_DURATION);

    public ScrapperImpl() {
        this.items.add(ItemStack.of(Material.STONE_SWORD, (byte) 1));
        this.items.add(ItemStack.of(Material.COOKED_PORKCHOP, (byte) 5));

        this.registerNode();
    }
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.SCRAPPER;
    }

    @Override
    public void applyEffects(Player player) {
        player.addEffect(SCRAPPER_SPEED);
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

        player.getInventory().setBoots(ItemStack.builder(Material.DIAMOND_BOOTS).meta(metaBuilder -> {
            metaBuilder.enchantment(Enchantment.FEATHER_FALLING, (short) 1);
        }).build());
    }
}
