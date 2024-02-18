package me.paradise.swagcraftsg.commands;

import me.paradise.swagcraftsg.commands.cmds.KitCommand;
import me.paradise.swagcraftsg.events.GamePhaseChangeEvent;
import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.MatchInvincibilityTimer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.GlobalEventHandler;

public class CommandManager {

    public CommandManager() {
        KitCommand kitCommand = new KitCommand();
        registerCommand(kitCommand);

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(GamePhaseChangeEvent.class, event -> {
            if(event.getNewState().equals(GamePhase.INGAME)) {
                unregisterCommand(kitCommand);
            }
        });
    }

    public static void registerCommand(Command command) {
        MinecraftServer.getCommandManager().register(command);
    }
    public static void unregisterCommand(Command command) {
        MinecraftServer.getCommandManager().unregister(command);
    }
}
