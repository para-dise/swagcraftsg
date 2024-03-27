package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.feature.tracker.PlayerTrackerCompass;
import me.paradise.swagcraftsg.kits.KitAbilityCooldown;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePlayableKit implements SwagCraftPlayableKit {
    private final static ItemStack PLAYER_TRACKER = new PlayerTrackerCompass().getItem();
    /*
        * The items that the kit will have, used by {@link #giveInventory(Player)}
     */
    protected final List<ItemStack> items = new ArrayList<>();
    /**
     * The global event node for the kit, registered in {@link #registerNode()}
     * The actual event listeners are registered in the implementing classes
     */
    protected EventNode<Event> globalNode;
    /**
     * A cooldown for the kit's ability, most kits don't use this
     */
    protected KitAbilityCooldown kitAbilityCooldown = new KitAbilityCooldown();

    /**
     * Put the kit's items into the player's inventory
     * @param player
     */
    @Override
    public void giveInventory(Player player) {
        player.getInventory().addItemStack(PLAYER_TRACKER);
        for(ItemStack item : this.items) {
            player.getInventory().addItemStack(item);
        }
    }

    public void registerNode() {
        this.globalNode = EventNode.all(this.getKit().name().toLowerCase() + "-global");
        MinecraftServer.getGlobalEventHandler().addChild(this.globalNode);
    }
}
