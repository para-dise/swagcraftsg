package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
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
import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PotionMeta;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArcherImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public ArcherImpl() {
        // 1x Bow
        // 24x Arrow
        // x5 Grilled Porkchops
        ItemStack bow = ItemStack.of(Material.BOW);
        this.items.add(bow);

        ItemStack arrows = ItemStack.of(Material.ARROW, 24);
        this.items.add(arrows);

        ItemStack porkchops = ItemStack.of(Material.COOKED_PORKCHOP, 5);
        this.items.add(porkchops);
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.ARCHER;
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
        EventNode<Event> archerGlobalNode = EventNode.all("archer-global-listener");

        archerGlobalNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
            if(!(event.getHitEntity() instanceof Player)) {
                return;
            }

            Player shooter = (Player) event.getEntity().getShooter();
            Player target = (Player) event.getHitEntity();

            if(shooter == null || target == null) {
                return;
            }

            if(KitChooser.getInstance().hasKit(shooter, this.getKit())) {

                if(isHeadshot(event.getEntity(), target)) {
                    shooter.sendMessage(Component.text("Bulls-eye!").color(NamedTextColor.RED));
                    target.kill();
                }

            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(archerGlobalNode);
    }

    private boolean isHeadshot(CustomEntityProjectile arrow, Player target) {
        double y = arrow.getPosition().y();
        double y2 = target.getEyeHeight();
        double y3 = target.getPosition().y() + y2;

        double distance = Math.abs(y - y3);
        return distance <= 0.5D;
    }
}
