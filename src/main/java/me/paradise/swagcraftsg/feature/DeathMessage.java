package me.paradise.swagcraftsg.feature;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class DeathMessage {
    public static Component getDeathMessageWithWeapon(Player player, Player killer, ItemStack weapon) {
        Component p1Name = player.getDisplayName() == null ? Component.text(player.getUsername(), NamedTextColor.RED) : player.getDisplayName();
        Component p2Name = killer.getDisplayName() == null ? Component.text(killer.getUsername(), NamedTextColor.RED) : killer.getDisplayName();

        return p1Name.append(Component.text(" was killed by ", NamedTextColor.DARK_RED)).append(p2Name).append(Component.text(" using ", NamedTextColor.DARK_RED)).append(normaliseWeaponName(weapon.getMaterial().name()));
    }

    private static Component normaliseWeaponName(String weaponName) {
        // remove minecraft:
        weaponName = weaponName.replace("minecraft:", "");
        // remove underscores and capitalize first letter of each word
        String[] words = weaponName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        return Component.text(sb.toString().trim(), NamedTextColor.RED);
    }
}
