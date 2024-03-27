package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

public class ButcherImpl extends BasePlayableKit {
    private Tag butcherMark = Tag.Byte("butcherMark");

    public ButcherImpl() {
        this.items.add(ItemStack.of(Material.IRON_AXE));

        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.BUTCHER;
    }

    @Override
    public void applyEffects(Player player) {

    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void registerGlobalListeners() {
        this.globalNode.addListener(FinalAttackEvent.class, event -> {
            if(!(event.getEntity() instanceof Player)) return;

            Player player = (Player) event.getEntity();
            if(!KitChooser.getInstance().hasKit(player, this.getKit())) return;
            if(event.getTarget() instanceof Player) return;

            // Allow butcher to two-shot animals
            if(event.getTarget().hasTag(this.butcherMark)) {
                // TODO: Make this better
                event.getTarget().remove();
            } else {
                event.getTarget().setTag(this.butcherMark, (byte) 1);
            }

        });
    }
}
