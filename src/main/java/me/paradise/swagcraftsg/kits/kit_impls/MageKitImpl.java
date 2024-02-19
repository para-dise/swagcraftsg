package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class MageKitImpl implements SwagCraftPlayableKit {

    private List<ItemStack> items = new ArrayList<>();

    public MageKitImpl() {
        this.items.add(ItemStack.of(Material.SNOWBALL, (byte) 16));
        this.items.add(ItemStack.of(Material.FIRE_CHARGE, (byte) 64));
        this.items.add(ItemStack.of(Material.FIRE_CHARGE, (byte) 64));
        this.items.add(ItemStack.of(Material.CLOCK, (byte) 3));
    }
    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.MAGE;
    }

    @Override
    public void applyEffects(Player player) {
        Potion regenPotion = new Potion(PotionEffect.REGENERATION, (byte) 0, 9999999);
        player.addEffect(regenPotion);
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
