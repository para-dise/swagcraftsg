package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.damage.CustomDamageType;
import io.github.bloepiloepi.pvp.events.FinalDamageEvent;
import me.paradise.swagcraftsg.kits.KitAbilityCooldown;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.utils.NearbyUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class HulkImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();
    private KitAbilityCooldown kitAbilityCooldown;

    public HulkImpl(KitAbilityCooldown kitAbilityCooldown) {
        this.kitAbilityCooldown = kitAbilityCooldown;

        this.items.add(ItemStack.of(Material.DIRT, (byte) 64));
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.HULK;
    }

    @Override
    public void applyEffects(Player player) {
        Potion strengthPot = new Potion(PotionEffect.STRENGTH, (byte) 0, 999999);
        Potion jumpPot = new Potion(PotionEffect.JUMP_BOOST, (byte) 0, 999999);

        player.addEffect(strengthPot);
        player.addEffect(jumpPot);
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
        EventNode<Event> globalNode = EventNode.all("hulk-global-listener");
        globalNode.addListener(FinalDamageEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) return;
            Player eventPlayer = (Player) event.getEntity();

            if(!KitChooser.getInstance().hasKit(eventPlayer, this.getKit())) return;

            if(event.getDamageType() == CustomDamageType.FALL) {
                eventPlayer.sendMessage(Component.text("HULK SMASH!").color(NamedTextColor.RED));

                for(Player player : NearbyUtil.getNearbyPlayers(eventPlayer, 10)) {
                    player.damage(DamageType.FALL, event.getDamage());
                }

                event.setDamage(2); // Reduced fall damage
            }
        });

        globalNode.addListener(PlayerStartDiggingEvent.class, event -> {
            Player player = event.getPlayer();

            if(!KitChooser.getInstance().hasKit(player, this.getKit())) return;
            if(this.kitAbilityCooldown.isOnCooldown(player.getUuid())) {
                player.sendMessage(Component.text("You are on cooldown for " + this.kitAbilityCooldown.getRemainingCooldown(player) + " seconds!").color(NamedTextColor.RED));
                return;
            }


            List<Player> nearby = NearbyUtil.getNearbyPlayers(player, 10);
            for(Player nearbyPlayer : nearby) {
                nearbyPlayer.damage(DamageType.GENERIC, 5);
            }

            player.sendMessage(Component.text("HULK SMASH!").color(NamedTextColor.RED));
            this.kitAbilityCooldown.useAbility(player);
        });

        // TODO: Add food regeneration

        MinecraftServer.getGlobalEventHandler().addChild(globalNode);
    }
}
