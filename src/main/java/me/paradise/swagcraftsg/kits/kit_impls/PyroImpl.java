package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntityFireEvent;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

public class PyroImpl extends BasePlayableKit {
    private final static Potion BASE_STRENGTH = new Potion(PotionEffect.STRENGTH, (byte) 0, 100);

    public PyroImpl() {
        this.items.add(ItemStack.of(Material.FLINT_AND_STEEL, (byte) 1));
        this.items.add(ItemStack.of(Material.COOKED_PORKCHOP, (byte) 5));

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.PYRO;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void giveInventory(Player player) {
        super.giveInventory(player);

        player.getInventory().setChestplate(ItemStack.builder(Material.LEATHER_CHESTPLATE).meta(metaBuilder -> {
            metaBuilder.enchantment(Enchantment.FIRE_PROTECTION, (short) 1);
        }).build());
    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void registerGlobalListeners() {
        this.globalNode.addListener(EntityFireEvent.class, event -> {
           if(!(event.getEntity() instanceof Player)) {
               return;
           }

           if(!KitChooser.getInstance().hasKit((Player) event.getEntity(), this.getKit())) {
               return;
           }

           final Player player = (Player) event.getEntity();
           player.addEffect(BASE_STRENGTH);
        });
    }
}
