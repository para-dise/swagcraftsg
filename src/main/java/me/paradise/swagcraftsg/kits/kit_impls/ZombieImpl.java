package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.listeners.CombatLogListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

public class ZombieImpl extends BasePlayableKit {
    private final Potion POISION_POTION = new Potion(PotionEffect.POISON, (byte) 0, 100);

    public ZombieImpl() {
        this.items.add(ItemStack.of(Material.ROTTEN_FLESH, (byte) 1));

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.ZOMBIE;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {
        MinecraftServer.getGlobalEventHandler().addChild(globalNode);
    }

    @Override
    public void registerGlobalListeners() {
        this.globalNode.addListener(FinalAttackEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) {
                return;
            }

            final Player player = (Player) event.getEntity();
            if(!KitChooser.getInstance().hasKit(player, this.getKit())) {
                return;
            }

            // 15% chance to infect
            if(Math.random() < 0.15) {
                player.sendMessage(Component.text("You have infected your target for 5 seconds!", NamedTextColor.GRAY));
                event.getTarget().addEffect(POISION_POTION);
            }

            // Heal half a heart
            player.setHealth(player.getHealth() + 1);

            // fist does same damage as a stone sword
            event.setBaseDamage(5);
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

        this.globalNode.addListener(PlayerDeathEvent.class, event -> {
            final Player p = event.getPlayer();
            if(CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.hasLog(p)) {
                Player killer = CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.getCombatant(p);

                if(!KitChooser.getInstance().hasKit(killer, this.getKit())) {
                    return;
                }

                // fill hunger
                killer.setFood(20);
            }
        });
    }
}
