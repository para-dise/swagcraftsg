package me.paradise.swagcraftsg.kits.kit_impls;

import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

public class GhostImpl extends BasePlayableKit {
    public GhostImpl() {
        this.registerNode();
    }

    @Override
    public SwagCraftKit getKit() {
        return SwagCraftKit.GHOST;
    }

    @Override
    public void applyEffects(Player player) {
        // TODO: Make this more accurate
        Potion invisibility = new Potion(PotionEffect.INVISIBILITY, (byte) 0, 900);
        player.addEffect(invisibility);
    }

    @Override
    public void registerListeners(Player player) {

    }

    @Override
    public void registerGlobalListeners() {

    }
}
