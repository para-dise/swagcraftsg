package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import io.github.bloepiloepi.pvp.projectile.CustomEntityProjectile;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class BarbarianImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public BarbarianImpl() {
        this.items.add(ItemStack.of(Material.STONE_SWORD));
        this.items.add(ItemStack.of(Material.BREAD, (byte) 3));
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
    public void giveInventory(Player player) {
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    @Override
    public void registerGlobalListeners() {

    }
}
