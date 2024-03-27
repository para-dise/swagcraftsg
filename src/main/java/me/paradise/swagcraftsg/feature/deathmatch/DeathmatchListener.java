package me.paradise.swagcraftsg.feature.deathmatch;

import me.paradise.swagcraftsg.chest.ChestManager;
import me.paradise.swagcraftsg.events.DeathmatchBeginEvent;
import me.paradise.swagcraftsg.events.GameDrawEvent;
import me.paradise.swagcraftsg.utils.TimeSync;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDeathEvent;

import java.util.Collection;

public class DeathmatchListener {
    private final static Component dmInvincibility = Component.text("Everyone is invincible for 10 seconds.", NamedTextColor.GREEN);
    public DeathmatchListener() {
        new DeathmatchBeginListener();
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerDeathEvent.class, event -> {
            Collection alivePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toList();

            if(alivePlayers.size() <= 5) {
                // Start deathmatch
                beginDeathMatch();
            }
        });

        globalEventHandler.addListener(GameDrawEvent.class, event -> {
            final Component drawMessage = Component.text("Game ended in a draw!", NamedTextColor.RED);
            for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                player.sendMessage(drawMessage);
                player.sendMessage(Component.text("Remaining players:", NamedTextColor.GRAY));

                for(Player alivePlayer : event.getRemainingPlayers()) {
                    player.sendMessage(Component.text("- " + alivePlayer.getUsername(), NamedTextColor.GRAY));

                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
        });
    }

    public static void beginDeathMatch() {
        if(!TimeSync.getInstance().isDeathmatch()) {
            TimeSync.getInstance().setDeathmatch(true);
            MinecraftServer.getGlobalEventHandler().call(new DeathmatchBeginEvent());

            for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                player.sendMessage(dmInvincibility);
            }
        }
    }
}
