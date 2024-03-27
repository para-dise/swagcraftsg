package me.paradise.swagcraftsg.listeners.vanilla;

import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.feature.fakeplayer.FakePlayer;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityFireEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.BlockActionPacket;
import net.minestom.server.network.packet.server.play.HitAnimationPacket;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.Direction;
import net.minestom.server.utils.time.TimeUnit;

import java.util.ArrayList;
import java.util.UUID;

public class BlockInteractListener {
    private final ArrayList<UUID> ENTITIES_ON_FIRE = new ArrayList<>();
    private static final Block FIRE = Block.FIRE;
    private static final Potion STRENGTH_POTION = new Potion(PotionEffect.STRENGTH, (byte) 0, 100);
    public BlockInteractListener() {
        EventNode<Event> blockInteractNode = EventNode.all("block_interact");
        // prevent chest breaking
        blockInteractNode.addListener(PlayerBlockBreakEvent.class, event -> {
            if(event.getBlock().compare(Block.CHEST)) {
                System.out.println("Chest breaking cancelled");
                event.setCancelled(true);
            }
        });

        // Handle trapdoors
        blockInteractNode.addListener(PlayerBlockInteractEvent.class, event -> {
            if(event.getBlock().getProperty("open") == null) return; // Not a [trap]door

            Block newBlock = event.getBlock().withProperty("open", event.getBlock().getProperty("open").equals("true") ? "false" : "true"); // Invert property
            event.getInstance().setBlock(event.getBlockPosition(), newBlock);

            if(newBlock.getProperty("half") == null) {
                return;
            }

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

        // handle door breaking
        blockInteractNode.addListener(PlayerBlockBreakEvent.class, event -> {
            if(event.getBlock().getProperty("half") == null) return; // Not a door

            if(event.getBlock().getProperty("half").equals("lower")) {
                // Break upper block
                Point blockPosition = event.getBlockPosition().add(0, 1, 0);
                event.getInstance().setBlock(blockPosition, Block.AIR);
            } else {
                // Break lower block
                Point blockPosition = event.getBlockPosition().add(0, -1, 0);
                event.getInstance().setBlock(blockPosition, Block.AIR);
            }
        });

        // drop broken blocks
        blockInteractNode.addListener(PlayerBlockBreakEvent.class, event -> {
            if(event.isCancelled()) return;

            Block block = event.getBlock();
            ItemStack itemStack = ItemStack.of(block.registry().material());

            ItemEntity itemEntity = new ItemEntity(itemStack.withAmount(1));
            itemEntity.setPickupDelay(2000, TimeUnit.MILLISECOND);
            itemEntity.setInstance(SwagCraftSG.MAIN_INSTANCE);
            itemEntity.spawn();

            Pos position = new Pos(event.getBlockPosition());
            itemEntity.teleport(position.add(0, 1, 0));
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

                // close in 5 seconds
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    event.getPlayer().sendPacketToViewersAndSelf(new BlockActionPacket(
                            event.getBlockPosition(),
                            (byte) 1,
                            (byte) 0,
                            event.getBlock()
                    ));
                }).delay(TaskSchedule.seconds(5)).schedule();
            }
        });

        // Flint and steel
        blockInteractNode.addListener(PlayerUseItemOnBlockEvent.class, event -> {
            if(event.getItemStack().isSimilar(ItemStack.of(Material.FLINT_AND_STEEL))) {
                BlockFace blockFace = event.getBlockFace();

                if(blockFace.toDirection().equals(Direction.UP)) {
                    Point blockPosition = event.getPosition().add(0, 1, 0);
                    event.getInstance().setBlock(blockPosition, Block.FIRE);

                    // Schedule deletion of fire in 300 ticks
                    MinecraftServer.getSchedulerManager().buildTask(() -> {
                        if(event.getInstance().getBlock(blockPosition).compare(Block.FIRE)) {
                            event.getInstance().setBlock(blockPosition, Block.AIR);
                        }
                    }).delay(TaskSchedule.tick(300)).schedule();
                }
            }
        });

        // Player walk into fire
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if(player.getGameMode().equals(GameMode.SURVIVAL)) {
                    Block below = player.getInstance().getBlock(player.getPosition());
                    if(below.compare(FIRE)) {
                        player.setFireForDuration(100);
                    }

                    if(player instanceof FakePlayer) continue;

                    for(Pos firePos : getNearbyFires(player.getPosition(), 1, player.getInstance(), player.getBoundingBox())) {
                        player.setFireForDuration(100);
                        //System.out.println("Player is colliding with fire" + isCollidingWithFire(player, firePos));
                    }
                }
            }
            return TaskSchedule.tick(10);
        });


        // Fire damage
        blockInteractNode.addListener(EntityFireEvent.class, event -> {
            if(event.isCancelled()) return;
            if(ENTITIES_ON_FIRE.contains(event.getEntity().getUuid())) return; // Already logged as on fire
            ENTITIES_ON_FIRE.add(event.getEntity().getUuid());

            SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
            schedulerManager.submitTask(() -> {
                if(event.getEntity() instanceof Player player) {
                    if(player.isOnFire()) {
                        if(!KitChooser.getInstance().hasKit(player, SwagCraftKit.PYRO)) {
                            player.damage(DamageType.ON_FIRE, 1);
                            // TODO: Verify this worked
                            player.playSound(Sound.sound(Key.key("entity.player.hurt_on_fire"), Sound.Source.PLAYER, 5f, 1f));
                            // Send Screen Shake
                            HitAnimationPacket hitAnimationPacket = new HitAnimationPacket(player.getEntityId(), 45);
                            player.sendPacket(hitAnimationPacket);

                            return TaskSchedule.tick(50);
                        } else {
                            player.addEffect(STRENGTH_POTION);
                        }
                    }
                }
                ENTITIES_ON_FIRE.remove(event.getEntity().getUuid());
                return TaskSchedule.stop();
            });
        });

        MinecraftServer.getGlobalEventHandler().addChild(blockInteractNode);
    }

    public static ArrayList<Pos> getNearbyFires(Pos centerPos, int range, Instance instance, BoundingBox boundingBox) {
        int centerX = (int) centerPos.x();
        int centerY = (int) centerPos.y();
        int centerZ = (int) centerPos.z();

        ArrayList<Pos> nearbyFires = new ArrayList<>();

        // Loop over nearest blocks ignoring y axis
        for(int x = centerX - range; x <= centerX + range; x++) {
            for(int z = centerZ - range; z <= centerZ + range; z++) {
                Pos blockPos = new Pos(x, centerY, z);
                if(instance.getBlock(blockPos).compare(FIRE)) {
                    nearbyFires.add(blockPos);
                }
            }
        }

        return nearbyFires;
    }

    @Deprecated
    public static boolean isCollidingWithFire(Player player, Pos firePos) {
        // do math to check if player is colliding with fire according to the bounding box (fire bounding box is 1x1x1)
        var playerBoundingBox = player.getBoundingBox();
        var fireBoundingBox = new BoundingBox(
                1, // width
                1, // height
                1 // depth/length
        );

        System.out.println("--> Begin position/bb dump <--");
        System.out.println("Player position: " + player.getPosition());

        System.out.println("[minX, minY, minZ]: " + playerBoundingBox.minX() + ", " + playerBoundingBox.minY() + ", " + playerBoundingBox.minZ());

        System.out.println("Fire position: " + firePos);
        System.out.println("[blockX, blockY, blockZ]: " + firePos.blockX() + ", " + firePos.blockY() + ", " + firePos.blockZ());

        System.out.println("Player bounding box: " + playerBoundingBox);
        System.out.println("Fire bounding box: " + fireBoundingBox);

        System.out.println("[maxX, maxY, maxZ]: " + playerBoundingBox.maxX() + ", " + playerBoundingBox.maxY() + ", " + playerBoundingBox.maxZ());


        System.out.println("--> End position dump <--");

        // https://developer.mozilla.org/en-US/docs/Games/Techniques/3D_collision_detection
        return firePos.blockX() >= playerBoundingBox.minX() - fireBoundingBox.maxX() &&
                firePos.blockX() <= playerBoundingBox.maxX() + fireBoundingBox.maxX() &&
                firePos.blockY() >= playerBoundingBox.minY() - fireBoundingBox.maxY() &&
                firePos.blockY() <= playerBoundingBox.maxY() + fireBoundingBox.maxY() &&
                firePos.blockZ() >= playerBoundingBox.minZ() - fireBoundingBox.maxZ() &&
                firePos.blockZ() <= playerBoundingBox.maxZ() + fireBoundingBox.maxZ();
    }
}
