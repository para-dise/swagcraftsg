package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.damage.CustomDamageType;
import io.github.bloepiloepi.pvp.events.EntityPreDeathEvent;
import io.github.bloepiloepi.pvp.events.ExplosionEvent;
import io.github.bloepiloepi.pvp.events.FinalDamageEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import io.github.bloepiloepi.pvp.explosion.PvpExplosionSupplier;
import io.github.bloepiloepi.pvp.projectile.CustomEntityProjectile;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.utils.NearbyUtil;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.Explosion;
import net.minestom.server.instance.ExplosionSupplier;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.network.packet.server.play.SoundEffectPacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.sound.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class CreeperImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public CreeperImpl() {
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.CREEPER;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void giveInventory(Player player) {
        for (ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    @Override
    public void registerGlobalListeners() {
        EventNode<Event> globalNode = EventNode.all("creeper-global-listener");

        globalNode.addListener(EntityPreDeathEvent.class, event -> {
            if (!(event.getEntity() instanceof Player)) return;

            Player player = (Player) event.getEntity();
            if (!KitChooser.getInstance().hasKit(player, this.getKit())) return;
            System.out.println(player.getUsername() + " died with the creeper kit");

            double x = player.getPosition().x();
            double y = player.getPosition().y();
            double z = player.getPosition().z();

            ParticlePacket packet = ParticleCreator.createParticlePacket(Particle.EXPLOSION, x, y, z, 0, 2, 0, 10);

            for (Player nearbyPlayer : NearbyUtil.getNearbyPlayers(player, 10)) {
                System.out.println("Sending packet to " + nearbyPlayer.getUsername());
                nearbyPlayer.getPlayerConnection().sendPacket(packet);
                nearbyPlayer.damage(Damage.fromPlayer(player, 10));
                nearbyPlayer.sendMessage(Component.text("You were hit by a creeper explosion!", NamedTextColor.RED));
            }
        });

        globalNode.addListener(FinalDamageEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();
            if(!KitChooser.getInstance().hasKit(player, this.getKit())) return;

            int random = (int) (Math.random() * 100) + 5;
            if(random <= 5) {
                // TODO: Add particles?
                event.setDamage(event.getDamage() + 2);
                player.sendMessage(Component.text("Ouch, you seem to have exploded a little!", NamedTextColor.RED));
            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(globalNode);
    }
}
