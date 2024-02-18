package me.paradise.swagcraftsg.listeners.vanilla;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.BlockActionPacket;

public class BlockInteractListener {
    public BlockInteractListener() {
        EventNode<Event> blockInteractNode = EventNode.all("block_interact");

        // Handle trapdoors
        blockInteractNode.addListener(PlayerBlockInteractEvent.class, event -> {
            if(event.getBlock().getProperty("open") == null) return; // Not a [trap]door

            Block newBlock = event.getBlock().withProperty("open", event.getBlock().getProperty("open").equals("true") ? "false" : "true"); // Invert property
            event.getInstance().setBlock(event.getBlockPosition(), newBlock);

            if(newBlock.getProperty("half").equals("upper")) {
                Point blockPosition = event.getBlockPosition().add(0, -1, 0);
                Block lowerBlock = event.getInstance().getBlock(blockPosition);

                // invert property
                lowerBlock = lowerBlock.withProperty("open", lowerBlock.getProperty("open").equals("true") ? "false" : "true");
                event.getInstance().setBlock(blockPosition, lowerBlock);
            } else if (newBlock.getProperty("half").equals("lower")) {
                Point blockPosition = event.getBlockPosition().add(0, 1, 0);
                Block upperBlock = event.getInstance().getBlock(blockPosition);

                // invert property
                upperBlock = upperBlock.withProperty("open", upperBlock.getProperty("open").equals("true") ? "false" : "true");
                event.getInstance().setBlock(blockPosition, upperBlock);
            }

        });

        // handle chests
        blockInteractNode.addListener(PlayerBlockInteractEvent.class, event -> {
            if(event.getBlock().compare(Block.CHEST)) {
                event.getPlayer().sendPacketToViewersAndSelf(new BlockActionPacket(
                        event.getBlockPosition(),
                        (byte) 1,
                        (byte) 1,
                        event.getBlock()
                ));
            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(blockInteractNode);
    }
}
