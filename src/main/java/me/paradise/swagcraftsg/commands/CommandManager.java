package me.paradise.swagcraftsg.commands;

import co.aikar.commands.MinestomCommandManager;
import com.google.common.collect.ImmutableList;
import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.commands.cmds.DebugCommand;
import me.paradise.swagcraftsg.commands.cmds.KitCommand;
import me.paradise.swagcraftsg.commands.cmds.acf.ACFDebugCommand;
import me.paradise.swagcraftsg.commands.cmds.acf.ACFSocTest;
import me.paradise.swagcraftsg.events.GamePhaseChangeEvent;
import me.paradise.swagcraftsg.match.GamePhase;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.GlobalEventHandler;

public class CommandManager {

    public CommandManager() {
        MinestomCommandManager commandManager = new MinestomCommandManager();
        KitCommand kitCommand = new KitCommand();
        registerCommand(kitCommand);

        if(SwagCraftSG.DEBUG) {
            System.out.println(MinecraftServer.LOGGER);
            MinecraftServer.LOGGER.info("Debug mode enabled");
            commandManager.registerCommand(new ACFDebugCommand());
            commandManager.registerCommand(new ACFSocTest());

            commandManager.getCommandCompletions().registerCompletion("debugging", c -> {
                return ImmutableList.of("drop", "timeRemaining");
            });

            commandManager.getCommandCompletions().registerCompletion("timeRemaining", c -> {
                return ImmutableList.of("set");
            });
            //DebugCommand debugCommand = new DebugCommand();
            //registerCommand(debugCommand);
        }

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(GamePhaseChangeEvent.class, event -> {
            if(event.getNewState().equals(GamePhase.INVINCIBILITY)) {
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
