package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import io.github.bloepiloepi.pvp.events.ProjectileHitEvent;
import io.github.bloepiloepi.pvp.potion.effect.CustomPotionEffect;
import io.github.bloepiloepi.pvp.potion.effect.CustomPotionEffects;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PotionMeta;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.PotionType;
import net.minestom.server.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlchemistImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();

    public AlchemistImpl() {
        this.items.add(ItemStack.of(Material.WOODEN_SWORD));

        // 1x Wood Sword
        //8x Instant Damage II Potions
        //10x Poision II Potions
        //10x Instant Health II Potions
        //10x Snowballs
        //5x Porkchops

        ItemMeta instantDamageMeta = new PotionMeta.Builder().potionType(PotionType.STRONG_HARMING).build();
        ItemStack pot1 = ItemStack.of(Material.SPLASH_POTION, 8).withMeta(instantDamageMeta);
        this.items.add(pot1);

        ItemMeta poisonMeta = new PotionMeta.Builder().potionType(PotionType.STRONG_POISON).build();
        ItemStack pot2 = ItemStack.of(Material.SPLASH_POTION, 10).withMeta(poisonMeta);
        this.items.add(pot2);

        ItemMeta instantHealthMeta = new PotionMeta.Builder().potionType(PotionType.STRONG_HEALING).build();
        ItemStack pot3 = ItemStack.of(Material.SPLASH_POTION, 10).withMeta(instantHealthMeta);
        this.items.add(pot3);

        ItemStack snowballs = ItemStack.of(Material.SNOWBALL, 10);
        this.items.add(snowballs);

        ItemStack porkchops = ItemStack.of(Material.PORKCHOP, 5);
        this.items.add(porkchops);
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.ALCHEMIST;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {
        EventNode<Event> alchemistPlayerNode = EventNode.all(player.getUsername() + "-kit");
        alchemistPlayerNode.addListener(FinalAttackEvent.class, event -> {
            if(!(event.getEntity() instanceof Player || !(event.getTarget() instanceof Player))) return;

            Player attacker = (Player) event.getEntity();
            Player target = (Player) event.getTarget();

            if(!KitChooser.getInstance().hasKit(attacker, this.getKit()) || !attacker.getUuid().equals(player.getUuid())) return;

            Random rand = new Random();
            final int randomNum = rand.nextInt(100);
            if(randomNum <= 20) {
                Potion potion = new Potion(PotionEffect.POISON, (byte) 0, 200);
                target.addEffect(potion);
                attacker.sendMessage(Component.text("You poisoned " + target.getUsername() + "!", NamedTextColor.GREEN));
            }

        });
        MinecraftServer.getGlobalEventHandler().addChild(alchemistPlayerNode);
    }

    @Override
    public void giveInventory(Player player) {
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    @Override
    public void registerGlobalListeners() {
        EventNode<Event> alchemistGlobalNode = EventNode.all("alchemist-global-listener");

        alchemistGlobalNode.addListener(ProjectileHitEvent.ProjectileEntityHitEvent.class, event -> {
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

                Potion potion = new Potion(PotionEffect.NAUSEA, (byte) 10, 200);
                target.addEffect(potion);

            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(alchemistGlobalNode);
    }
}
