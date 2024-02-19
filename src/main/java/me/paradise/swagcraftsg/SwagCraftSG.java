package me.paradise.swagcraftsg;

import io.github.bloepiloepi.pvp.PvpExtension;
import io.github.bloepiloepi.pvp.explosion.PvpExplosionSupplier;
import me.paradise.swagcraftsg.commands.CommandManager;
import me.paradise.swagcraftsg.map.SwagCraftMap;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import me.paradise.swagcraftsg.scoreboard.ScoreboardDisplay;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;

public class SwagCraftSG {
    public static Instance MAIN_INSTANCE;

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        // Create Map Manager
        SwagCraftMap mapManager = new SwagCraftMap();
        Instance lobby = mapManager.getMapInstance();
        lobby.setExplosionSupplier(PvpExplosionSupplier.INSTANCE);

        MAIN_INSTANCE = lobby; // Set the main instance

        // Create the instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.AIR));
        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();

            if(!Match.getGamePhase().equals(GamePhase.WAITING)) {
                player.setGameMode(GameMode.SPECTATOR);
            }

            event.setSpawningInstance(MAIN_INSTANCE);
            //event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(mapManager.getSpawnPoint(player.getUuid()));
        });
        // Enable PVP
        PvpExtension.init();
        MinecraftServer.getGlobalEventHandler().addChild(PvpExtension.legacyEvents());

        // Enable commands
        new Match();
        new CommandManager();

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);

        // enable scoreboard
        ScoreboardDisplay scoreboardDisplay = new ScoreboardDisplay();
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            scoreboardDisplay.display(player);

            // fix a bug..?
            ItemEntity itemEntity = new ItemEntity(ItemStack.of(Material.AIR));
            itemEntity.setPickupDelay(0, TimeUnit.MILLISECOND);
            itemEntity.setInstance(SwagCraftSG.MAIN_INSTANCE);
            itemEntity.spawn();
            itemEntity.teleport(player.getPosition());
        });
    }

    public static boolean isDebugMode() {
        return true;
    }
}