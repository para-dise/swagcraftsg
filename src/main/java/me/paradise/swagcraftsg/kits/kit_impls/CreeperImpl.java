package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.EntityPreDeathEvent;
import io.github.bloepiloepi.pvp.events.FinalDamageEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.utils.DamageUtil;
import me.paradise.swagcraftsg.utils.NearbyUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

public class CreeperImpl extends BasePlayableKit {

    public CreeperImpl() {
        this.registerNode();
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
    public void registerGlobalListeners() {
        this.globalNode.addListener(EntityPreDeathEvent.class, event -> {
            if (!(event.getEntity() instanceof Player)) return;

            Player player = (Player) event.getEntity();
            if (!KitChooser.getInstance().hasKit(player, this.getKit())) return;
            System.out.println(player.getUsername() + " died with the creeper kit");

            double x = player.getPosition().x();
            double y = player.getPosition().y();
            double z = player.getPosition().z();

            ParticlePacket packet = new ParticlePacket(Particle.EXPLOSION, (float) x, (float) y, (float) z, 0, 2, 0, 10, 10);
            //ParticlePacket packet = ParticleCreator.createParticlePacket(Particle.EXPLOSION, x, y, z, 0, 2, 0, 10);

            for (Player nearbyPlayer : NearbyUtil.getNearbyPlayers(player, 10)) {
                System.out.println("Sending packet to " + nearbyPlayer.getUsername());
                nearbyPlayer.getPlayerConnection().sendPacket(packet);
                DamageUtil.damage(nearbyPlayer, new Damage(DamageType.PLAYER_EXPLOSION, null, player, null, 10));
                //nearbyPlayer.damage(Damage.fromPlayer(player, 10));
                nearbyPlayer.sendMessage(Component.text("You were hit by a creeper explosion!", NamedTextColor.RED));

                // throw player randomly
                Vec v = nearbyPlayer.getPosition().direction();
                nearbyPlayer.setVelocity(v.mul(5));
            }
        });

        this.globalNode.addListener(FinalDamageEvent.class, event -> {
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
    }
}
