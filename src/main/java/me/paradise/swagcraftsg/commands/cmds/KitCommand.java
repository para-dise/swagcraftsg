package me.paradise.swagcraftsg.commands.cmds;

import me.paradise.swagcraftsg.match.GamePhase;
import me.paradise.swagcraftsg.match.Match;
import me.paradise.swagcraftsg.menus.KitChooseMenu;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

public class KitCommand extends Command {
    public KitCommand() {
        super("kit");
        setDefaultExecutor((sender, context) -> {
            if(!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return;
            }

            Player player = (Player) sender;
            if(!(player.getGameMode().equals(GameMode.SURVIVAL))) {
                player.sendMessage("You cannot use this command if you are dead!");
                return;
            }

            KitChooseMenu menu = new KitChooseMenu();
            player.openInventory(menu.getInventory());
        });
    }
}
