package me.paradise.swagcraftsg.kits.kit_impls;

import io.github.bloepiloepi.pvp.events.FinalAttackEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class ButcherImpl implements SwagCraftPlayableKit {
    private List<ItemStack> items = new ArrayList<>();
    private Tag butcherMark = Tag.Byte("butcherMark");

    public ButcherImpl() {
        this.items.add(ItemStack.of(Material.IRON_AXE));
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
    public void giveInventory(Player player) {
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    @Override
    public void registerGlobalListeners() {
        EventNode<Event> globalNode = EventNode.all("butcher-global");

        globalNode.addListener(FinalAttackEvent.class, event -> {
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

        MinecraftServer.getGlobalEventHandler().addChild(globalNode);
    }
}
