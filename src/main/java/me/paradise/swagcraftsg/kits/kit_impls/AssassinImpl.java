package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.damage.CustomDamageType;
import io.github.bloepiloepi.pvp.events.FinalDamageEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

public class AssassinImpl extends BasePlayableKit {
    public AssassinImpl() {
        // 1 golden enchanted sword (Smite 1, Sharpness 5)
        ItemStack sword = ItemStack.builder(Material.GOLDEN_SWORD).meta(metaBuilder -> {
            metaBuilder.enchantment(Enchantment.SMITE, (byte) 0);
            metaBuilder.enchantment(Enchantment.SHARPNESS, (byte) 4);
        }).build();
        items.add(sword);

        // 4x Apples
        ItemStack apple = ItemStack.of(Material.APPLE, 4);
        items.add(apple);

        // Bow
        ItemStack bow = ItemStack.of(Material.BOW);
        items.add(bow);

        // 10 arrows
        ItemStack arrows = ItemStack.of(Material.ARROW, 10);
        items.add(arrows);

        // 16 snowballs
        ItemStack snowballs = ItemStack.of(Material.SNOWBALL, 16);
        items.add(snowballs);

        // x5 Porkchops
        ItemStack porkchops = ItemStack.of(Material.PORKCHOP, 5);
        items.add(porkchops);

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.ASSASSIN;
    }

    @Override
    public void applyEffects(Player player) {
        Potion speedPot = new Potion(PotionEffect.SPEED, (byte) 1, 50000);
        Potion jumpPot = new Potion(PotionEffect.JUMP_BOOST, (byte) 0, 50000);
        player.addEffect(speedPot);
        player.addEffect(jumpPot);
    }

    @Override
    public void registerListeners(Player player) {
        EventNode<Event> assassinPlayerNode = EventNode.all(player.getUsername() + "-kit");
        assassinPlayerNode.addListener(PlayerUseItemEvent.class, event -> {
            Player eventPlayer = event.getPlayer();

            if(!KitChooser.getInstance().hasKit(eventPlayer, this.getKit()) || !eventPlayer.getUuid().equals(player.getUuid())) return;

            if(event.getItemStack().isSimilar(ItemStack.of(Material.APPLE))) {

                Potion invisPot = new Potion(PotionEffect.INVISIBILITY, (byte) 0, 300);
                player.addEffect(invisPot);

                // Consume item
                for(int i = 0; i < eventPlayer.getInventory().getSize(); i++) {
                    ItemStack item = eventPlayer.getInventory().getItemStack(i);
                    if(item.isSimilar(ItemStack.of(Material.APPLE))) {
                        eventPlayer.getInventory().setItemStack(i, item.consume(1));
                        break;
                    }
                }

                eventPlayer.sendMessage(Component.text("Whoosh").color(NamedTextColor.GREEN));
            } else if(event.getItemStack().isSimilar(ItemStack.of(Material.POTATO))) {
                // TODO: Only do this at night
                Potion nightVisionPot = new Potion(PotionEffect.NIGHT_VISION, (byte) 0, 300);
                player.addEffect(nightVisionPot);

                // Consume item
                for(int i = 0; i < eventPlayer.getInventory().getSize(); i++) {
                    ItemStack item = eventPlayer.getInventory().getItemStack(i);
                    if(item.isSimilar(ItemStack.of(Material.POTATO))) {
                        eventPlayer.getInventory().setItemStack(i, item.consume(1));
                        break;
                    }
                }
            }

        });

        MinecraftServer.getGlobalEventHandler().addChild(assassinPlayerNode);
    }

    @Override
    public void registerGlobalListeners() {
        this.globalNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
            if(!(event.getHitEntity() instanceof Player)) {
                return;
            }

            Player shooter = (Player) event.getEntity().getShooter();
            Player target = (Player) event.getHitEntity();

            if(shooter == null || target == null) {
                return;
            }

            if(KitChooser.getInstance().hasKit(shooter, this.getKit()) && event.getEntity().getEntityType().equals(EntityType.SNOWBALL)) {
                shooter.sendMessage(Component.text("You made " + target.getUsername() + " nauseous with your snowballs!", NamedTextColor.RED));

                Potion potion = new Potion(PotionEffect.BLINDNESS, (byte) 10, 100);
                target.addEffect(potion);

            }
        });

        this.globalNode.addListener(FinalDamageEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) return;
            Player eventPlayer = (Player) event.getEntity();

            if(!KitChooser.getInstance().hasKit(eventPlayer, this.getKit())) return;

            if(event.getDamageType() == CustomDamageType.FALL && event.getDamage() >= 2) {
                eventPlayer.sendMessage(Component.text("Well, that would've been a rough fall").color(NamedTextColor.RED));
                event.setDamage(2);
            }
        });
    }
}
