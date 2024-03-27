package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import io.github.bloepiloepi.pvp.projectile.CustomEntityProjectile;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ArcherImpl extends BasePlayableKit {
    public ArcherImpl() {
        ItemStack bow = ItemStack.of(Material.BOW);
        this.items.add(bow);

        ItemStack arrows = ItemStack.of(Material.ARROW, 24);
        this.items.add(arrows);

        ItemStack porkchops = ItemStack.of(Material.COOKED_PORKCHOP, 5);
        this.items.add(porkchops);

        this.registerNode();
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
    public void registerGlobalListeners() {
        this.globalNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
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
    }

    private boolean isHeadshot(CustomEntityProjectile arrow, Player target) {
        double y = arrow.getPosition().y();
        double y2 = target.getEyeHeight();
        double y3 = target.getPosition().y() + y2;

        double distance = Math.abs(y - y3);
        return distance <= 0.5D;
    }
}
