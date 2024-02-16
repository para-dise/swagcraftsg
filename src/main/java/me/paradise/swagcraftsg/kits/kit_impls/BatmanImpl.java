package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import me.paradise.swagcraftsg.events.GamePhaseChangeEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.utils.DisguiseUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerStartSneakingEvent;
import net.minestom.server.event.player.PlayerStopSneakingEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class BatmanImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public BatmanImpl() {
        this.items.add(ItemStack.of(Material.STONE_SWORD));
        this.items.add(ItemStack.of(Material.BREAD, (byte) 3));

        this.items.add(ItemStack.of(Material.BOW));
        this.items.add(ItemStack.of(Material.ARROW, (byte) 16));
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.BATMAN;
    }

    @Override
    public void applyEffects(Player player) {
    }

    @Override
    public void registerListeners(Player player) {
        EventNode<Event> batmanPlayerNode = EventNode.all(player.getUsername() + "-kit");

        batmanPlayerNode.addListener(GamePhaseChangeEvent.class, event -> {
           if(event.getNewState().equals(GamePhase.INGAME)) {
               player.clearEffects();
               //MinecraftServer.getGlobalEventHandler().removeChild(batmanPlayerNode);
           }
        });

        batmanPlayerNode.addListener(PlayerStartSneakingEvent.class, event -> {
            Player eventPlayer = event.getPlayer();
            if(KitChooser.getInstance().hasKit(eventPlayer, this.getKit()) && eventPlayer.getUuid().equals(player.getUuid())) {
                DisguiseUtil disguiseUtil = KitChooser.getInstance().getDisguiseUtil();
                disguiseUtil.disguisePlayer(eventPlayer, EntityType.BAT);
            }
        });

        batmanPlayerNode.addListener(PlayerStopSneakingEvent.class, event -> {
            Player eventPlayer = event.getPlayer();
            if(KitChooser.getInstance().hasKit(eventPlayer, this.getKit()) && eventPlayer.getUuid().equals(player.getUuid())) {
                DisguiseUtil disguiseUtil = KitChooser.getInstance().getDisguiseUtil();
                disguiseUtil.undisguisePlayer(eventPlayer);
            }
        });

        batmanPlayerNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
           if(!((event.getHitEntity()) instanceof Player)) return;

              Player hitPlayer = (Player) event.getHitEntity();
              if(KitChooser.getInstance().hasKit(hitPlayer, this.getKit()) && hitPlayer.getUuid().equals(player.getUuid())) {
                  player.damage(Damage.fromEntity(event.getEntity().getShooter(), 2));
                  event.setCancelled(true);
              }
        });

        MinecraftServer.getGlobalEventHandler().addChild(batmanPlayerNode);
    }

    @Override
    public void giveInventory(Player player) {
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    @Override
    public void registerGlobalListeners() {
        EventNode<Event> batmanGlobalNode = EventNode.all("batman-global");
        batmanGlobalNode.addListener(PlayerChangeHeldSlotEvent.class, event -> {
            // TODO: This gets called twice...
            if(KitChooser.getInstance().getCombatLogListener().getCombatLogManager().isInCombat(event.getPlayer())) {
                // if the attacker is a batman, we cancel the event

                Player player = KitChooser.getInstance().getCombatLogListener().getCombatLogManager().getCombatant(event.getPlayer());
                if (player != null && KitChooser.getInstance().hasKit(player, this.getKit())) {
                    player.sendMessage(Component.text("Your hands have been frozen by Batman!"));
                    event.setCancelled(true);
                }
            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(batmanGlobalNode);
    }

    public void applyPregameEffects(Player player) {
        Potion invisibilityPotion = new Potion(PotionEffect.INVISIBILITY, (byte) 0, 9999999);
        player.addEffect(invisibilityPotion);
    }
}
