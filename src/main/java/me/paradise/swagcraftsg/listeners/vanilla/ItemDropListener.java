package me.paradise.swagcraftsg.listeners.vanilla;

import me.paradise.swagcraftsg.SwagCraftSG;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;

public class ItemDropListener {
    public ItemDropListener() {
        EventNode<Event> dropNode = EventNode.all("item_drop");

        // Handle item drops
        dropNode.addListener(ItemDropEvent.class, event -> {
            ItemStack itemStack = event.getItemStack();
            Pos position = event.getPlayer().getPosition();

            // Drop the items
            for(int i = 0; i < itemStack.amount(); i++) {
                ItemEntity itemEntity = new ItemEntity(itemStack.withAmount(1));
                itemEntity.setPickupDelay(2000, TimeUnit.MILLISECOND);
                itemEntity.setInstance(SwagCraftSG.MAIN_INSTANCE);
                itemEntity.spawn();
                itemEntity.teleport(position.add(0, 1, 0));
            }
        });

        dropNode.addListener(PickupItemEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) return;
            final Player player = (Player) event.getEntity();

            if(!player.getGameMode().equals(GameMode.SURVIVAL)) {
                event.setCancelled(true);
                return;
            }

            ((Player) event.getEntity()).getInventory().addItemStack(event.getItemStack());
        });

        MinecraftServer.getGlobalEventHandler().addChild(dropNode);
    }
}
