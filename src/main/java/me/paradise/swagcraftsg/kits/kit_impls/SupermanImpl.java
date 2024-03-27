package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.damage.CustomDamageType;
import io.github.bloepiloepi.pvp.damage.CustomIndirectEntityDamage;
import io.github.bloepiloepi.pvp.events.FinalDamageEvent;
import io.github.bloepiloepi.pvp.projectile.ThrownPotion;
import me.paradise.swagcraftsg.kits.KitAbilityCooldown;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.timer.SchedulerManager;

import java.time.temporal.ChronoUnit;

public class SupermanImpl extends BasePlayableKit {
    private KitAbilityCooldown kitAbilityCooldown = new KitAbilityCooldown(90000);
    private static final ItemStack SUPERMAN_FEATHER = ItemStack.builder(Material.FEATHER).meta(meta -> {
        meta.displayName(Component.text("Superman's Feather").decoration(TextDecoration.ITALIC, false));
        meta.lore(Component.text("Right click to fly!").decoration(TextDecoration.ITALIC, false));
        meta.lore(Component.text("Flight time: 5s").decoration(TextDecoration.ITALIC, false));
    }).build();

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.SUPERMAN;
    }

    public SupermanImpl() {
        this.items.add(SUPERMAN_FEATHER);

        this.registerNode();
    }

    @Override
    public void applyEffects(Player player) {
        Potion strength = new Potion(PotionEffect.STRENGTH, (byte) 1, Potion.INFINITE_DURATION);
        player.addEffect(strength);

        Potion speed = new Potion(PotionEffect.SPEED, (byte) 0, Potion.INFINITE_DURATION);
        player.addEffect(speed);

        Potion regen = new Potion(PotionEffect.REGENERATION, (byte) 0, Potion.INFINITE_DURATION);
        player.addEffect(regen);
    }

    @Override
    public void registerListeners(Player player) {
        this.globalNode.addListener(FinalDamageEvent.class, event -> {
            if(!KitChooser.getInstance().hasKit(player, SwagCraftKit.SUPERMAN)) return;

            if(event.getDamageType().equals(CustomDamageType.FALL)) {
                event.setDamage(event.getDamage() * 2);
            } else if(event.getDamageType() instanceof CustomIndirectEntityDamage magic) {
                if(magic.getDirectEntity() instanceof ThrownPotion) {
                    event.setCancelled(true);
                }
            }
        });

        this.globalNode.addListener(PlayerUseItemEvent.class, event -> {
            if(!KitChooser.getInstance().hasKit(player, SwagCraftKit.SUPERMAN)) return;

            if(this.kitAbilityCooldown.isOnCooldown(player.getUuid())) {
                player.sendMessage(Component.text("You can't use this ability yet!").decoration(TextDecoration.ITALIC, false));
                return;
            }
            this.kitAbilityCooldown.useAbility(player);

            if(event.getItemStack().isSimilar(SUPERMAN_FEATHER)) {
                // throw player up 1 block
                Vec velocity = player.getVelocity();
                player.setVelocity(velocity.withY(1.5f));
                player.setFlying(true);
                player.sendMessage(Component.text("You are now flying!").decoration(TextDecoration.ITALIC, false));

                SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();

                schedulerManager.buildTask(() -> {
                    player.setFlying(false);
                    player.sendMessage(Component.text("You are no longer flying!").decoration(TextDecoration.ITALIC, false));
                }).delay(5, ChronoUnit.SECONDS).schedule();
            }
        });
    }

    @Override
    public void registerGlobalListeners() {

    }

    @Override
    public void giveInventory(Player player) {
        super.giveInventory(player);

        player.setChestplate(ItemStack.builder(Material.GOLDEN_CHESTPLATE).meta(meta -> {
            meta.enchantment(Enchantment.UNBREAKING, (short) 3);
        }).build());
    }
}
