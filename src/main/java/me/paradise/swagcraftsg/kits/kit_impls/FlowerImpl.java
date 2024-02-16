package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.List;

public class FlowerImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public FlowerImpl() {
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.FLOWER;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            for(ItemStack itemStack : player.getInventory().getItemStacks()) {
                // TODO: Make this work
            }

            return TaskSchedule.seconds(5);
        });
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
