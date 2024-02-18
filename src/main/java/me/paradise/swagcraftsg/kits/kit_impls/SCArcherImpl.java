package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import me.paradise.swagcraftsg.utils.ExplosionUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class SCArcherImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public SCArcherImpl() {
        this.items.add(ItemStack.of(Material.BOW, (byte) 1));
        this.items.add(ItemStack.of(Material.ARROW, (byte) 24));
        this.items.add(ItemStack.of(Material.DIAMOND_PICKAXE, (byte) 1));
        this.items.add(ItemStack.of(Material.ENDER_PEARL, (byte) 5));
        this.items.add(ItemStack.of(Material.COOKED_PORKCHOP, (byte) 5));
        this.items.add(ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE, (byte) 1));
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
        EventNode<Event> scarcherGlobalNode = EventNode.all("scarcher-global-listener");
        System.out.println("registered scarcher global node");
        scarcherGlobalNode.addListener(ProjectileHitEvent.ProjectileBlockHitEvent.class, event -> {
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

        MinecraftServer.getGlobalEventHandler().addChild(scarcherGlobalNode);
    }

    @Override
    public void giveInventory(Player player) {
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    @Override
    public void registerGlobalListeners() {

    }
}
