package me.paradise.swagcraftsg.entities;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.goal.RandomLookAroundGoal;

public class BatmanBat extends EntityCreature {

    public BatmanBat() {
        super(EntityType.BAT);
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new RandomLookAroundGoal(this, 20))
                        .build()
        );
    }

    public String getEntityPlayer() {
        return "owo";
    }
}