package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import me.paradise.swagcraftsg.kits.KitAbilityCooldown;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ThorImpl extends BasePlayableKit {
    private final static Sound THUNDER_SOUND = Sound.sound(Key.key("entity.lightning_bolt.impact"), Sound.Source.HOSTILE, 5f, 2f);
    public ThorImpl() {
        this.items.add(ItemStack.builder(Material.IRON_AXE).displayName(Component.text("Thor's Hammer").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)).build());
        this.items.add(ItemStack.of(Material.COOKED_PORKCHOP, (byte) 5));

        this.kitAbilityCooldown = new KitAbilityCooldown(30000);
        registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.THOR;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void registerGlobalListeners() {
        this.globalNode.addListener(PlayerUseItemEvent.class, event -> {
            final Player player = event.getPlayer();

            if (!KitChooser.getInstance().hasKit(player, this.getKit())) {
                return;
            }

            if (!event.getItemStack().getMaterial().equals(Material.IRON_AXE)) {
                return;
            }

            if(this.kitAbilityCooldown.isOnCooldown(player.getUuid())) {
                player.sendMessage(Component.text("You are on cooldown for " + this.kitAbilityCooldown.getRemainingCooldown(player) + " seconds!").color(NamedTextColor.RED));
                return;
            }
            this.kitAbilityCooldown.useAbility(player);

            // get block player is looking at
            Point target = player.getTargetBlockPosition(20);
            if(target != null) {
                Entity lightning = new Entity(EntityType.LIGHTNING_BOLT);
                lightning.setInstance(player.getInstance());
                Pos pos = new Pos(target.x(), target.y(), target.z());
                lightning.teleport(pos);
                lightning.spawn();

                player.getInstance().getNearbyEntities(target, 3).forEach(entity -> {
                    if(entity instanceof Player p) {
                        p.damage(DamageType.LIGHTNING_BOLT, 5);
                        p.playSound(THUNDER_SOUND);
                    }
                });
            }
        });
        this.globalNode.addListener(FinalAttackEvent.class, event -> {
            if(!(event.getTarget() instanceof Player)) {
                return;
            }

            final Player player = (Player) event.getTarget();
            if(!KitChooser.getInstance().hasKit(player, this.getKit())) {
                return;
            }

            // reduce incoming damage by 10%
            event.setBaseDamage(event.getBaseDamage() * 0.9f);
        });
        this.globalNode.addListener(FinalAttackEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) {
                return;
            }

            final Player player = (Player) event.getEntity();
            if(!KitChooser.getInstance().hasKit(player, this.getKit())) {
                return;
            }

            // fist does same damage as a stone sword
            event.setBaseDamage(5);
        });
    }
}
