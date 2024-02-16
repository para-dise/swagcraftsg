package me.paradise.swagcraftsg.listeners;

import me.paradise.swagcraftsg.events.GameWinEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.entity.metadata.other.FallingBlockMeta;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.*;

public class GlobalDeathListener {

    private HashMap<UUID, Inventory> cachedinventories = new HashMap<>();
    private Tag<UUID> deathTag = Tag.UUID("death");

    public GlobalDeathListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerDeathEvent.class, event -> {
            // Create chest with player's items
            String username = PlainTextComponentSerializer.plainText().serialize(event.getPlayer().getName());
            Inventory inventory = new Inventory(InventoryType.CHEST_5_ROW, username + "'s Inventory");

            event.getPlayer().getInventory().addItemStack(ItemStack.of(Material.DIAMOND));

            for(ItemStack itemStack : event.getPlayer().getInventory().getItemStacks()) {
                inventory.addItemStack(itemStack);
            }


            // New fallingblock
            Entity fallingBlock = new Entity(EntityType.FALLING_BLOCK);
            FallingBlockMeta meta = (FallingBlockMeta) fallingBlock.getEntityMeta();
            meta.setHasNoGravity(true);
            meta.setBlock(Block.BEACON.withTag(deathTag, event.getPlayer().getUuid()));
            meta.setInvisible(false);
            fallingBlock.setInstance(event.getPlayer().getInstance(), event.getPlayer().getPosition().add(.5, 0, .5));
            fallingBlock.spawn();

            cachedinventories.put(event.getPlayer().getUuid(), inventory);

            event.setDeathText(Component.empty());
            event.getPlayer().setGameMode(GameMode.SPECTATOR);

            // Check if the game is finished
            Collection alivePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toList();
            if(alivePlayers.size() == 1) {
                MinecraftServer.getGlobalEventHandler().call(new GameWinEvent((Player) alivePlayers.toArray()[0]));
            }
        });

        globalEventHandler.addListener(PlayerEntityInteractEvent.class, event -> {
           if(event.getTarget().getEntityType().equals(EntityType.FALLING_BLOCK) && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
               FallingBlockMeta meta = (FallingBlockMeta) event.getTarget().getEntityMeta();
               if(meta.getBlock().id() == Block.BEACON.id() && meta.getBlock().hasTag(deathTag)) {
                   UUID uuid = meta.getBlock().getTag(deathTag);
                   Inventory inventory = cachedinventories.get(uuid);
                   event.getPlayer().openInventory(cachedinventories.get(uuid));
               }
           }
        });
    }
}
