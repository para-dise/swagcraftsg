package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import me.paradise.swagcraftsg.utils.ExplosionUtil;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class SCArcherImpl extends BasePlayableKit {
    public SCArcherImpl() {
        this.items.add(ItemStack.of(Material.BOW, (byte) 1));
        this.items.add(ItemStack.of(Material.ARROW, (byte) 24));
        this.items.add(ItemStack.of(Material.DIAMOND_PICKAXE, (byte) 1));
        this.items.add(ItemStack.of(Material.ENDER_PEARL, (byte) 5));
        this.items.add(ItemStack.of(Material.COOKED_PORKCHOP, (byte) 5));
        this.items.add(ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE, (byte) 1));

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.SC_ARCHER;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {
        this.globalNode.addListener(ProjectileHitEvent.ProjectileBlockHitEvent.class, event -> {
            if(!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }

            if(!Match.getGamePhase().equals(GamePhase.INGAME)) {
                return;
            }


            Player shooter = (Player) event.getEntity().getShooter();
            if(KitChooser.getInstance().hasKit(shooter, this.getKit())) {
                // TNT arrow
                Pos pos = event.getEntity().getPosition();
                ExplosionUtil.explode((float) pos.x(), (float) pos.y(), (float) pos.z(), 3.0f, shooter.getInstance(), shooter);
                //MinecraftServer.getGlobalEventHandler().call(new ExplosionEvent(event.getInstance(), Collections.singletonList((Point) pos)));
                //event.getEntity().getInstance().explode((float) pos.x(), (float) pos.y(), (float) pos.z(), 3.0f);
            }
        });
    }

    @Override
    public void registerGlobalListeners() {

    }
}
