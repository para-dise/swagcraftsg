package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.LegacyKnockbackEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import io.github.bloepiloepi.pvp.projectile.Snowball;
import me.paradise.swagcraftsg.entities.projectile.Fireball;
import me.paradise.swagcraftsg.kits.KitAbilityCooldown;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.UUID;

public class MageKitImpl extends BasePlayableKit {
    public MageKitImpl() {
        this.items.add(ItemStack.of(Material.SNOWBALL, (byte) 16));
        this.items.add(ItemStack.of(Material.FIRE_CHARGE, (byte) 64));
        this.items.add(ItemStack.of(Material.FIRE_CHARGE, (byte) 64));
        this.items.add(ItemStack.of(Material.CLOCK, (byte) 3));
        this.kitAbilityCooldown = new KitAbilityCooldown(30000);

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.MAGE;
    }

    @Override
    public void applyEffects(Player player) {
        Potion regenPotion = new Potion(PotionEffect.REGENERATION, (byte) 0, Potion.INFINITE_DURATION);
        player.addEffect(regenPotion);
    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void registerGlobalListeners() {

    }

    @Override
    public void registerInGameListeners() {
        // Clock using
        this.globalNode.addListener(PlayerUseItemEvent.class, event -> {
            final Player player = event.getPlayer();

            if(!KitChooser.getInstance().hasKit(player, this.getKit())) {
                return;
            }

            if(!event.getItemStack().getMaterial().equals(Material.CLOCK)) {
                return;
            }

            if(this.kitAbilityCooldown.isOnCooldown(player.getUuid())) {
                player.sendMessage(Component.text("You are on cooldown for " + this.kitAbilityCooldown.getRemainingCooldown(player) + " seconds!").color(NamedTextColor.RED));
                return;
            }

            int count = freezePlayersInArea(player.getPosition(), 30, player);
            player.sendMessage(Component.text(count + " players have been frozen in a radius of 30 blocks").color(NamedTextColor.GRAY));
            this.kitAbilityCooldown.useAbility(player);
        });
        this.globalNode.addListener(ProjectileHitEvent.ProjectileBlockHitEvent.class, event -> {
            if(!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }

            if(!(event.getEntity() instanceof Fireball)) {
                return;
            }

            Player shooter = (Player) event.getEntity().getShooter();
            if(KitChooser.getInstance().hasKit(shooter, this.getKit())) {
                Fireball fireball = (Fireball) event.getEntity();
                fireball.explodeArea();
            }
        });
        // Firecharge handling
        this.globalNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
            if(!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }

            if(!(event.getEntity() instanceof Fireball)) {
                return;
            }

            Player shooter = (Player) event.getEntity().getShooter();
            if(KitChooser.getInstance().hasKit(shooter, this.getKit())) {
                Fireball fireball = (Fireball) event.getEntity();
                fireball.explodeArea();
            }
        });
        // Firecharge throwing
        this.globalNode.addListener(PlayerUseItemEvent.class, event -> {
            final Player player = event.getPlayer();

            if(!KitChooser.getInstance().hasKit(player, this.getKit())) {
                return;
            }

            if(!event.getItemStack().getMaterial().equals(Material.FIRE_CHARGE)) {
                return;
            }

            // remove firecharge from inventory
            player.getInventory().setItemInMainHand(player.getItemInMainHand().consume(1));

            Fireball fireball = new Fireball(event.getPlayer(), 15);
            Pos position = event.getPlayer().getPosition();
            Vec direction = event.getPlayer().getPosition().direction();
            fireball.setVelocity(direction.normalize().mul(20));
            fireball.setGravity(0.0, 0.0);
            fireball.setInstance(event.getPlayer().getInstance(), position.add(0, event.getPlayer().getEyeHeight(),0));
        });
        // Snowballs
        globalNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
            if(!Match.getGamePhase().equals(GamePhase.INGAME)) {
                return;
            }

            if(!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }

            if(!(event.getEntity() instanceof Snowball)) {
                return;
            }

            Player shooter = (Player) event.getEntity().getShooter();
            if(KitChooser.getInstance().hasKit(shooter, this.getKit())) {
                Potion blindnessPotion = new Potion(PotionEffect.BLINDNESS, (byte) 0, 30);
                event.getHitEntity().addEffect(blindnessPotion);
            }
        });
    }

    private int freezePlayersInArea(Pos pos, double radius, Player ignorePlayer) {
        ArrayList<UUID> frozenPlayers = new ArrayList<>();
        EventNode<Event> moveNode = EventNode.all("mage-freeze-listener-" + ignorePlayer.getUuid());
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if(player.getUuid().equals(ignorePlayer.getUuid())) {
                continue;
            }

            if(player.getPosition().distance(pos) <= radius) {
                frozenPlayers.add(player.getUuid());
            }
        }
        moveNode.addListener(PlayerMoveEvent.class, event -> {
            if(frozenPlayers.contains(event.getPlayer().getUuid())) {
                event.setCancelled(true);
            }
        });

        moveNode.addListener(LegacyKnockbackEvent.class, event -> {
            Entity hit = event.getEntity();

            if(frozenPlayers.contains(hit.getUuid())) {
                event.setCancelled(true);
            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(moveNode);

        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.scheduleTask(() -> {
            MinecraftServer.getGlobalEventHandler().removeChild(moveNode);
        }, TaskSchedule.seconds(5), TaskSchedule.stop());
        return frozenPlayers.size();
    }
}
