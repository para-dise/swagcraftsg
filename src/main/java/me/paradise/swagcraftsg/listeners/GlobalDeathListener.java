package me.paradise.swagcraftsg.listeners;

import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.events.GameWinEvent;
import me.paradise.swagcraftsg.feature.DeathMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;

import java.util.Collection;

public class GlobalDeathListener {
    private DeathMessage deathMessage = new DeathMessage();

    public GlobalDeathListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerDeathEvent.class, event -> {
            final Player p = event.getPlayer();

            if(CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.hasLog(p)) {
                Player killer = CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.getCombatant(p);
                if(killer != null) {
                    event.setChatMessage(DeathMessage.getDeathMessageWithWeapon(p, killer, killer.getInventory().getItemInMainHand()));
                } else {
                    event.setChatMessage(Component.text(p.getUsername() + " was killed by an unknown entity"));
                }
            } else {
                event.setChatMessage(Component.text(p.getUsername() + " was killed by an unknown entity"));
            }

            // drop items
            for(ItemStack itemStack : event.getPlayer().getInventory().getItemStacks()) {
                if(itemStack.isAir()) continue;

                ItemEntity itemEntity = new ItemEntity(itemStack);
                itemEntity.setPickupDelay(500, TimeUnit.MILLISECOND);
                itemEntity.setInstance(SwagCraftSG.MAIN_INSTANCE);
                itemEntity.spawn();
                itemEntity.teleport(event.getPlayer().getPosition().add(0, 1, 0));

                System.out.println("Dropped " + itemStack.getMaterial().name() + " at " + event.getPlayer().getPosition());
            }

            event.getPlayer().getInventory().clear();

            event.setDeathText(Component.empty());
            event.getPlayer().setGameMode(GameMode.SPECTATOR);

            // Check if the game is finished
            Collection alivePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toList();
            if(alivePlayers.size() == 1) {
                MinecraftServer.getGlobalEventHandler().call(new GameWinEvent((Player) alivePlayers.toArray()[0]));
            }
        });

        globalEventHandler.addListener(PickupItemEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) return;
            final Player player = (Player) event.getEntity();

            if(!player.getGameMode().equals(GameMode.SURVIVAL)) {
                event.setCancelled(true);
                return;
            }

            ((Player) event.getEntity()).getInventory().addItemStack(event.getItemStack());
        });
    }
}
