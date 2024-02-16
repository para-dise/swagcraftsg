package me.paradise.swagcraftsg.match;

import lombok.Getter;
import lombok.Setter;
import me.paradise.swagcraftsg.commands.CommandManager;
import me.paradise.swagcraftsg.commands.cmds.ShopCommand;
import me.paradise.swagcraftsg.events.GamePhaseChangeEvent;
import me.paradise.swagcraftsg.kits.KitChooser;
import me.paradise.swagcraftsg.listeners.GameWinListener;
import me.paradise.swagcraftsg.listeners.GlobalDeathListener;
import me.paradise.swagcraftsg.listeners.PlayerUseChestListener;
import me.paradise.swagcraftsg.listeners.PreGameStartDamageListener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.fakeplayer.FakePlayer;

import java.util.UUID;

public class Match {
    private final KitChooser kitChooser = KitChooser.getInstance();
    private MatchStarter matchStarter = new MatchStarter(this);
    private MatchInvincibilityTimer matchInvincibilityTimer = new MatchInvincibilityTimer(this);
    public static @Getter @Setter GamePhase gamePhase = GamePhase.WAITING;

    public Match() {
        this.setPhase(GamePhase.WAITING);
        new PreGameStartDamageListener(this);
    }

    public void setPhase(GamePhase phase) {
        MinecraftServer.getGlobalEventHandler().call(new GamePhaseChangeEvent(this.gamePhase, phase));
        switch (phase) {
            case WAITING -> {
                gamePhase = GamePhase.WAITING;
                matchStarter.startCountdown();
            }
            case INVINCIBILITY -> {
                gamePhase = GamePhase.INVINCIBILITY;
                KitChooser.getInstance().initKits();
                KitChooser.getInstance().applyKits();
                matchInvincibilityTimer.startTimer();

                new PlayerUseChestListener();
                CommandManager.registerCommand(new ShopCommand());
                refreshCommands();
            }
            case INGAME -> {
                gamePhase = GamePhase.INGAME;
                this.registerListeners();
            }
        }
    }

    public void populateLobby(int amount) {
        for(int i = 0; i < amount; i++) {
            this.spawnNPC();
        }
    }

    private void spawnNPC() {
        UUID uuid = UUID.randomUUID();
        FakePlayer.initPlayer(
                uuid,
                "npc-" + uuid.toString().substring(0, 4),
                fakePlayer -> {
                    fakePlayer.setAutoViewable(true);
                    // add fakePlayer to connection manager
                    //MinecraftServer.getConnectionManager().registerPlayer(fakePlayer);
                    //Instance instance = MinecraftServer.getInstanceManager().getInstances().iterator().next();
                    //fakePlayer.setInstance(instance);
                });
    }

    public void registerListeners() {
        new GlobalDeathListener();
        new GameWinListener();
    }

    public void refreshCommands() {
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.refreshCommands();
        }
    }
}
