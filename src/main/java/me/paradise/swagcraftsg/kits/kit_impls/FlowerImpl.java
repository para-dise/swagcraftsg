package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.TaskSchedule;

public class FlowerImpl extends BasePlayableKit {

    public FlowerImpl() {
        this.registerNode();
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
    public void registerGlobalListeners() {

    }
}
