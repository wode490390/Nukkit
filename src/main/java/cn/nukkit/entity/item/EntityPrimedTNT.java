package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

/**
 * @author MagicDroidX
 */
public class EntityPrimedTNT extends Entity implements EntityExplosive {

    public static final int NETWORK_ID = TNT;

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    public float getLength() {
        return 0.98f;
    }

    @Override
    public float getHeight() {
        return 0.98f;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    protected float getDrag() {
        return 0.02f;
    }

    @Override
    protected float getBaseOffset() {
        return 0.49f;
    }

    @Override
    public boolean canCollide() {
        return false;
    }

    protected int fuse;

    protected Entity source;

    public EntityPrimedTNT(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityPrimedTNT(FullChunk chunk, CompoundTag nbt, Entity source) {
        super(chunk, nbt);
        this.source = source;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return source.getCause() == DamageCause.VOID && super.attack(source);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void setOnFire(int seconds) {

    }

    @Override
    public boolean canBeMovedByCurrents() {
        return false;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.namedTag.contains("Fuse")) {
            this.fuse = this.namedTag.getByte("Fuse");
        } else {
            this.fuse = 80;
        }

        this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, true);
        this.setDataProperty(new IntEntityData(DATA_FUSE_LENGTH, this.fuse));

        this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_FIZZ);
    }


    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        namedTag.putByte("Fuse", fuse);
    }

    @Override
    public boolean onUpdate(int currentTick) {

        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }

        if (this.fuse % 5 == 0) {
            this.setDataProperty(new IntEntityData(DATA_FUSE_LENGTH, this.fuse));
        }

        this.lastUpdate = currentTick;

        boolean hasUpdate = entityBaseTick(tickDiff);

        if (this.isAlive()) {

            this.motionY -= this.getGravity();

            this.move(this.motionX, this.motionY, this.motionZ);

            float friction = 1 - this.getDrag();

            this.motionX *= friction;
            this.motionY *= friction;
            this.motionZ *= friction;

            this.updateMovement();

            if (this.onGround) {
                this.motionY *= -0.5;
                this.motionX *= 0.7;
                this.motionZ *= 0.7;
            }

            this.fuse -= tickDiff;

            if (this.fuse <= 0) {
                if (this.level.getGameRules().getBoolean(GameRule.TNT_EXPLODES)) {
                    this.explode();
                }
                this.kill();
            }

        }

        this.timing.stopTiming();

        return hasUpdate || this.fuse >= 0 || Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionY) > 0.00001 || Math.abs(this.motionZ) > 0.00001;
    }

    @Override
    public void explode() {
        EntityExplosionPrimeEvent event = new EntityExplosionPrimeEvent(this, 4);
        this.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        Explosion explosion = new Explosion(this, event.getForce(), this);
        if (event.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
    }

    public Entity getSource() {
        return this.source;
    }
}
