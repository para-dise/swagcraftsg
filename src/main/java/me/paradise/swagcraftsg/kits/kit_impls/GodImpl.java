package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class GodImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public GodImpl() {
        ItemStack sword = ItemStack.builder(Material.DIAMOND_SWORD).meta(metaBuilder -> {
            metaBuilder.enchantment(Enchantment.SMITE, (byte) 1);
            metaBuilder.enchantment(Enchantment.SHARPNESS, (byte) 5);
            metaBuilder.enchantment(Enchantment.FLAME, (byte) 5);
            metaBuilder.enchantment(Enchantment.KNOCKBACK, (byte) 5);
            metaBuilder.enchantment(Enchantment.BANE_OF_ARTHROPODS, (byte) 1);
        }).build();

        ItemStack bow = ItemStack.builder(Material.BOW).meta(metaBuilder -> {
            metaBuilder.enchantment(Enchantment.INFINITY, (byte) 1);
            metaBuilder.enchantment(Enchantment.FLAME, (byte) 1);
            metaBuilder.enchantment(Enchantment.PUNCH, (byte) 2);
            metaBuilder.enchantment(Enchantment.POWER, (byte) 1);
        }).build();

        this.items.add(sword);
        this.items.add(bow);

        this.items.add(ItemStack.of(Material.ARROW));
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.GOD;
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

        player.setHelmet(this.godEnchant(Material.DIAMOND_HELMET));
        player.setChestplate(this.godEnchant(Material.DIAMOND_CHESTPLATE));
        player.setLeggings(this.godEnchant(Material.DIAMOND_LEGGINGS));
        player.setBoots(this.godEnchant(Material.DIAMOND_BOOTS));
    }

    @Override
    public void registerGlobalListeners() {

    }

    private ItemStack godEnchant(Material material) {
        return ItemStack.builder(material).meta(metaBuilder -> {
            metaBuilder.enchantment(Enchantment.PROTECTION, (byte) 1);
            metaBuilder.enchantment(Enchantment.FIRE_PROTECTION, (byte) 1);
            metaBuilder.enchantment(Enchantment.BLAST_PROTECTION, (byte) 1);
            metaBuilder.enchantment(Enchantment.PROJECTILE_PROTECTION, (byte) 1);
            metaBuilder.enchantment(Enchantment.RESPIRATION, (byte) 3);
            metaBuilder.enchantment(Enchantment.AQUA_AFFINITY, (byte) 2);
            metaBuilder.enchantment(Enchantment.THORNS, (byte) 1);
        }).build();
    }
}
