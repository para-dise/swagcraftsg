package me.paradise.swagcraftsg.feature.tracker;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.SpawnPositionPacket;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerTrackerCompass {
    @Getter private final ItemStack item;

    public PlayerTrackerCompass() {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Right click to track the nearest player"));

        this.item = ItemStack.builder(Material.COMPASS)
                .displayName(Component.text("Player Tracker"))
                .lore(lore)
                .build();

        track();
    }

    public void track() {
        EventNode<Event> node = EventNode.all("player-tracker-compass");

        node.addListener(PlayerUseItemEvent.class, event -> {
            final Player player = event.getPlayer();
            AtomicReference<Player> closest = new AtomicReference<>(null);

            if (event.getItemStack().isSimilar(item)) {
                // find nearest player
                player.getInstance().getNearbyEntities(player.getPosition(), 100).stream()
                        .filter(entity -> entity instanceof Player)
                        .map(entity -> (Player) entity)
                        .filter(entity -> !entity.getUuid().equals(player.getUuid()))
                        .forEach(entity -> {
                            if (closest.get() == null) {
                                closest.set(entity);
                            } else if (player.getPosition().distance(entity.getPosition()) < player.getPosition().distance(closest.get().getPosition())) {
                                closest.set(entity);
                            }
                        });

                if(closest.get() == null) {
                    player.sendMessage(Component.text("No players nearby", NamedTextColor.YELLOW));
                    return;
                }

                player.sendMessage(Component.text("Compass pointing at " + closest.get().getUsername(), NamedTextColor.YELLOW));
                player.sendPacket(new SpawnPositionPacket(closest.get().getPosition().asVec(), 0));
            }
        });

        MinecraftServer.getGlobalEventHandler().addChild(node);
    }

}
