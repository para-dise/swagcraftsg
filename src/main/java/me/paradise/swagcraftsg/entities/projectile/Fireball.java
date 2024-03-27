package me.paradise.swagcraftsg.entities.projectile;

import io.github.bloepiloepi.pvp.projectile.CustomEntityProjectile;
import io.github.bloepiloepi.pvp.projectile.ItemHoldingProjectile;
import me.paradise.swagcraftsg.utils.ExplosionUtil;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.item.FireballMeta;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Fireball extends CustomEntityProjectile implements ItemHoldingProjectile {

    private int maxBlocksTravelled = 100;
    private Pos startPos;

    public Fireball(@Nullable Entity shooter) {
        super(shooter, EntityType.FIREBALL, true);

        this.startPos = shooter.getPosition();
    }

    public Fireball(@Nullable Entity shooter, int maxBlocksTravelled) {
        super(shooter, EntityType.FIREBALL, true);
        this.maxBlocksTravelled = maxBlocksTravelled;

        this.startPos = shooter.getPosition();
    }

    @Override
    public void onHit(Entity entity) {
        triggerStatus((byte) 3);

        remove();
    }

    @Override
    public void onStuck() {
        remove();
    }

    @Override
    public void tick(long time) {
        super.tick(time);

        if(startPos.distance(getPosition()) > maxBlocksTravelled) {
            remove();
            this.explodeArea();
        }

    }

    @Override
    public void setItem(@NotNull ItemStack item) {
        ((FireballMeta) getEntityMeta()).setItem(item);
    }

    public void explodeArea() {
        Pos pos = getPosition();
        ExplosionUtil.explode((float) pos.x(), (float) pos.y(), (float) pos.z(), 3.0f, getShooter().getInstance(), getShooter());

        if(!this.removed) {
            remove();
        }
    }
}