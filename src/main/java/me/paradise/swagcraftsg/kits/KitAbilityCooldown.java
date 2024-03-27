package me.paradise.swagcraftsg.kits;

import net.minestom.server.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class KitAbilityCooldown {
    private HashMap<UUID, Date> cooldownMap = new HashMap<>();
    private static int DEFAULT_COOLDOWN = 10000;

    public KitAbilityCooldown() {
    }

    public KitAbilityCooldown(int cooldown) {
        DEFAULT_COOLDOWN = cooldown;
    }

    public boolean isOnCooldown(UUID uuid) {
        if(cooldownMap.containsKey(uuid)) {
            Date date = cooldownMap.get(uuid);
            if(date.getTime() + 10000 < new Date().getTime()) {
                cooldownMap.remove(uuid);
                return false;
            }
            return true;
        }
        return false;
    }

    public void useAbility(Player player) {
        cooldownMap.put(player.getUuid(), new Date());
    }

    public int getRemainingCooldown(Player player) {
        if(cooldownMap.containsKey(player.getUuid())) {
            Date date = cooldownMap.get(player.getUuid());
            int cdTime =  (int) (DEFAULT_COOLDOWN - (new Date().getTime() - date.getTime()));
            return cdTime / 1000;
        }
        return 0;
    }
}
