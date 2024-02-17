package me.paradise.swagcraftsg.entities;

import lombok.Getter;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;

public class BatmanBat extends EntityCreature {
    @Getter private Player owner;

    public BatmanBat(Player owner) {
        super(EntityType.BAT);

        this.owner = owner;
    }
}