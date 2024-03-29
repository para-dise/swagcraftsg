package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import me.paradise.swagcraftsg.kits.KitAbilityCooldown;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.utils.NearbyUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

import java.util.List;

public class IronmanImpl extends BasePlayableKit {

    public IronmanImpl(KitAbilityCooldown kitAbilityCooldown) {
        this.items.add(ItemStack.of(Material.BREAD, (byte) 3));
        this.kitAbilityCooldown = kitAbilityCooldown;

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.IRONMAN;
    }

    @Override
    public void applyEffects(Player player) {
        Potion jumpPotion = new Potion(PotionEffect.JUMP_BOOST, (byte) 0, Potion.INFINITE_DURATION);
        Potion speedPotion = new Potion(PotionEffect.SPEED, (byte) 0, Potion.INFINITE_DURATION);

        player.addEffect(jumpPotion);
        player.addEffect(speedPotion);
    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void registerGlobalListeners() {
        this.globalNode.addListener(FinalAttackEvent.class, event -> {
           if(!(event.getEntity() instanceof Player)) {
               return;
           }

           Player player = (Player) event.getEntity();

           if(!KitChooser.getInstance().hasKit(player, this.getKit())) {
               return;
           }

           if(((Player) event.getEntity()).getItemInMainHand().isSimilar(ItemStack.AIR) && ((Player) event.getEntity()).getItemInOffHand().isSimilar(ItemStack.AIR)) {
               event.setBaseDamage((float) (event.getBaseDamage() + 5));
           }
        });

        // 48 blocks is the Minecraft audible range
        this.globalNode.addListener(PlayerBlockInteractEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) {
                return;
            }

            Player player = (Player) event.getEntity();

            if(!KitChooser.getInstance().hasKit(player, this.getKit())) {
                return;
            }

            if(this.kitAbilityCooldown.isOnCooldown(player.getUuid())) {
                player.sendMessage(Component.text("You are on cooldown for " + this.kitAbilityCooldown.getRemainingCooldown(player) + " seconds!").color(NamedTextColor.RED));
                return;
            }

            List<Player> nearbyPlayers = NearbyUtil.getNearbyPlayers(player, 48);
            Potion nauseaPotion = new Potion(PotionEffect.NAUSEA, (byte) 0, 300);
            for(Player nearbyPlayer : nearbyPlayers) {
                nearbyPlayer.addEffect(nauseaPotion);
            }
            player.sendMessage(Component.text("You applied nausea to " + nearbyPlayers.size() + " players!", NamedTextColor.RED));
            this.kitAbilityCooldown.useAbility(player);
        });

        // TODO: Scramble compasses
    }
}
