package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import io.github.bloepiloepi.pvp.projectile.CustomEntityProjectile;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.utils.ConsumeUtil;
import me.paradise.swagcraftsg.utils.WoodUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.PotionType;
import net.minestom.server.potion.TimedPotion;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.List;

public class BruteImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public BruteImpl() {
        ItemStack cookies = ItemStack.of(Material.COOKIE, 5);
        this.items.add(cookies);

        ItemStack porkchops = ItemStack.of(Material.COOKED_PORKCHOP, 4);
        this.items.add(porkchops);
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.BRUTE;
    }

    @Override
    public void applyEffects(Player player) {
        Potion slowness = new Potion(PotionEffect.SLOWNESS, (byte) 0, 9999999);
        player.addEffect(slowness);
    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void giveInventory(Player player) {
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }

        player.setBoots(ItemStack.of(Material.IRON_BOOTS));
        player.setLeggings(ItemStack.of(Material.IRON_LEGGINGS));
        player.setChestplate(ItemStack.of(Material.IRON_CHESTPLATE));
        player.setHelmet(ItemStack.of(Material.IRON_HELMET));
    }

    @Override
    public void registerGlobalListeners() {
        EventNode<Event> bruteGlobalNode = EventNode.all("brute-global-listener");

        bruteGlobalNode.addListener(PlayerUseItemEvent.class, event -> {
            Player player = event.getPlayer();
            if(KitChooser.getInstance().hasKit(player, this.getKit()) && event.getItemStack().isSimilar(ItemStack.of(Material.COOKIE))) {
                Potion strength = new Potion(PotionEffect.STRENGTH, (byte) 1, 300);
                player.addEffect(strength);

                ConsumeUtil.consume(player, event.getItemStack());

                player.sendMessage(Component.text("Nom nom nom", NamedTextColor.RED));
            }
        });

        bruteGlobalNode.addListener(PlayerBlockBreakEvent.class, event -> {
           if(KitChooser.getInstance().hasKit(event.getPlayer(), this.getKit())) {
               if(WoodUtil.isWood(event.getBlock())) {
                   this.breakTree(event.getBlockPosition(), event.getInstance(), 0);
               }
           }
        });

        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {

            for(Player player : KitChooser.getInstance().getPlayersWithKit(this.getKit())) {
                if(player == null) {
                    System.out.println("Player is null" + player);
                    continue;
                }
                List<TimedPotion> effects = player.getActiveEffects();
                boolean hasSlowness = false;
                for(TimedPotion effect : effects) {
                    if(effect.getPotion().effect() == PotionEffect.SLOWNESS) {
                        hasSlowness = true;
                        break;
                    }
                }

                if(!hasSlowness) {
                    Potion slowness = new Potion(PotionEffect.SLOWNESS, (byte) 0, 9999999);
                    player.addEffect(slowness);
                }
            }

            return TaskSchedule.seconds(5);
        });

        MinecraftServer.getGlobalEventHandler().addChild(bruteGlobalNode);
    }

    private void breakTree(Point pos, Instance instance, int depth) {
        if(!WoodUtil.isWood(instance.getBlock(pos)) || depth > 10) {
            return;
        }
        // Loop through all blocks in a 3x3x3 cube around the block
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    Point newPos = pos.add(x, y, z);
                    if(WoodUtil.isWood(instance.getBlock(newPos))) {
                        instance.setBlock(newPos, Block.AIR);
                        breakTree(newPos, instance, depth + 1);
                    }
                }
            }
        }
    }
}
