package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class GhostImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public GhostImpl() {
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.GHOST;
    }

    @Override
    public void applyEffects(Player player) {
        // TODO: Make this more accurate
        Potion invisibility = new Potion(PotionEffect.INVISIBILITY, (byte) 0, 900);
        player.addEffect(invisibility);
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
