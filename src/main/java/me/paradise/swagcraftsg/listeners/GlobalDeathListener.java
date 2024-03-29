package me.paradise.swagcraftsg.listeners;

import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.events.GameWinEvent;
import me.paradise.swagcraftsg.feature.DeathMessage;
import me.paradise.swagcraftsg.feature.fakeplayer.FakePlayer;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.kits.SwagCraftKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GlobalDeathListener {
    public GlobalDeathListener() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();

            if(player.getGameMode().equals(GameMode.SURVIVAL)) {
                if(CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.hasLog(player)) {
                    Player attacker = CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.getCombatant(player);

                    if(attacker != null) {
                        for(Player p : onlinePlayers) {
                            p.sendMessage(Component.text(player.getUsername()).append(Component.text(" tried to escape ", NamedTextColor.RED).append(Component.text(attacker.getUsername(), NamedTextColor.DARK_RED))));
                        }
                    } else {
                        onlinePlayers.forEach(p -> p.sendMessage(Component.text(player.getUsername()).append(Component.text(" committed suicide."))));
                    }
                } else {
                    onlinePlayers.forEach(p -> p.sendMessage(Component.text(player.getUsername()).append(Component.text(" committed suicide."))));
                }
            }

            checkForWin();
        });

        globalEventHandler.addListener(PlayerDeathEvent.class, event -> {
            final Player p = event.getPlayer();
            Component username = Component.text(p.getUsername(), NamedTextColor.RED);

            if(CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.hasLog(p)) {
                Player killer = CombatLogListener.SIMPLE_COMBAT_LOG_MANAGER.getCombatant(p);

                if(killer != null) {
                    event.setChatMessage(DeathMessage.getDeathMessageWithWeapon(p, killer, killer.getInventory().getItemInMainHand()));
                } else {
                    event.setChatMessage(username.append(Component.text(" was killed by an unknown entity", NamedTextColor.DARK_RED)));
                }
            } else {
                event.setChatMessage(username.append(Component.text(" was killed by an unknown entity", NamedTextColor.DARK_RED)));
            }

            // drop items
            if(!KitChooser.getInstance().hasKit(p, SwagCraftKit.MISER)) {
                for(ItemStack itemStack : event.getPlayer().getInventory().getItemStacks()) {
                    if(itemStack.isAir()) continue;

                    ItemEntity itemEntity = new ItemEntity(itemStack);
                    itemEntity.setPickupDelay(500, TimeUnit.MILLISECOND);
                    itemEntity.setInstance(SwagCraftSG.MAIN_INSTANCE);
                    itemEntity.spawn();
                    itemEntity.teleport(event.getPlayer().getPosition().add(0, 1, 0));

                    //System.out.println("Dropped " + itemStack.getMaterial().name() + " at " + event.getPlayer().getPosition());
                }
            }

            event.getPlayer().getInventory().clear();

            event.setDeathText(Component.empty());
            event.getPlayer().setGameMode(GameMode.SPECTATOR);

            // Check if the game is finished
            checkForWin();

            // Remove fakeplayer
            if(p instanceof FakePlayer fakePlayer) {
                fakePlayer.remove();
            }
        });
    }

    private void checkForWin() {
        Collection<@NotNull Player> alivePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toList();
        if(alivePlayers.size() == 1) {
            MinecraftServer.getGlobalEventHandler().call(new GameWinEvent((Player) alivePlayers.toArray()[0]));
        }
    }
}
