package me.paradise.swagcraftsg.commands.cmds;

import me.paradise.swagcraftsg.shop.Shop;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class ShopCommand extends Command {
    private final Shop shop = new Shop();
    public ShopCommand() {
        super("shop");
        setDefaultExecutor((sender, context) -> {
            if(!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return;
            }

            Player player = (Player) sender;
            player.openInventory(shop.getInventory());
        });
    }
}
