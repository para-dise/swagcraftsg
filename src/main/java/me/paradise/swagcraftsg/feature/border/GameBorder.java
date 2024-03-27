package me.paradise.swagcraftsg.feature.border;

import kotlin.time.TimeSource;
import lombok.Getter;
import lombok.Setter;
import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.events.GameDrawEvent;
import me.paradise.swagcraftsg.events.GameWinEvent;
import me.paradise.swagcraftsg.feature.deathmatch.DeathmatchListener;
import me.paradise.swagcraftsg.map.gson.MapData;
import me.paradise.swagcraftsg.utils.DamageUtil;
import me.paradise.swagcraftsg.utils.TimeSync;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.WorldBorder;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameBorder {
    @Getter
    private static final GameBorder instance = new GameBorder();
    private final ArrayList<Integer> SHRINK_TIMES = new ArrayList<>();
    @Getter @Setter
    private AtomicInteger gameProgress = new AtomicInteger(25 * 60);
    private final static int MAX_BORDER_SIZE = 517;
    private int borderSize = MAX_BORDER_SIZE;

    public GameBorder() {
        SHRINK_TIMES.add(25 * 60);
        SHRINK_TIMES.add(20 * 60);
        SHRINK_TIMES.add(15 * 60);
        SHRINK_TIMES.add(10 * 60);
        SHRINK_TIMES.add(5 * 60);
        SHRINK_TIMES.add(3 * 60);
        SHRINK_TIMES.add(60);
        SHRINK_TIMES.add(30);
        SHRINK_TIMES.add(15);
        SHRINK_TIMES.add(10);
        SHRINK_TIMES.add(5);
        SHRINK_TIMES.add(3);
        SHRINK_TIMES.add(1);
    }

    public void initializeBorder() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        scheduler.submitTask(() -> {
            if(SHRINK_TIMES.contains(gameProgress.get())) {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> {
                    player.sendMessage(Component.text("World-Border Shrinked!", NamedTextColor.GREEN));
                });

                Vec center = SwagCraftSG.MAP_MANAGER.getCenter();

                WorldBorder border = SwagCraftSG.MAIN_INSTANCE.getWorldBorder();
                border.setCenter((float) center.x(), (float) center.z());

                // decrease by 10% of the current size
                borderSize = (int) (borderSize * 0.9);
                SwagCraftSG.MAIN_INSTANCE.getWorldBorder().setDiameter(borderSize);
            }

            for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                // if they are outside the border, teleport them to the center
                if(!SwagCraftSG.MAIN_INSTANCE.getWorldBorder().isInside(player.getPosition())) {
                    player.sendMessage(Component.text("You are outside the border!", NamedTextColor.RED));

                    if(player.getGameMode().equals(GameMode.SURVIVAL)) {
                        Damage borderDamage = new Damage(DamageType.OUTSIDE_BORDER, null, null, null, 5);
                        DamageUtil.damage(player, borderDamage);
                    }
                }
            }

            if(gameProgress.get() <= 0) {
                if(TimeSync.getInstance().isDeathmatch()) {
                    GameDrawEvent event = new GameDrawEvent(MinecraftServer.getConnectionManager().getOnlinePlayers().stream().filter(p -> p.getGameMode().equals(GameMode.SURVIVAL)).collect(Collectors.toList()));
                    MinecraftServer.getGlobalEventHandler().call(event);

                    return TaskSchedule.stop();
                }

                gameProgress.set(25 * 60);
                DeathmatchListener.beginDeathMatch();
            }

            gameProgress.set(gameProgress.get() - 1);
            if(gameProgress.get() / 60 > 0) {
                TimeSync.getInstance().setTime((int) gameProgress.get() / 60);
            } else {
                // negative time
                TimeSync.getInstance().setTime((int) -gameProgress.get());
            }

            return TaskSchedule.seconds(1);
        });
    }
}
