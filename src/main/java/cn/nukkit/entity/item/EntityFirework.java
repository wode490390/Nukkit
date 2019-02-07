package cn.nukkit.entity.item;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.data.SlotEntityData;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author CreeperFace
 */
public class EntityFirework extends Entity {

    public static final int NETWORK_ID = FIREWORKS_ROCKET;

    private int fireworkAge;
    private int lifetime;
    private Item firework;

    public EntityFirework(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.fireworkAge = 0;

        this.motionX = ThreadLocalRandom.current().nextGaussian() * 0.001d;
        this.motionZ = ThreadLocalRandom.current().nextGaussian() * 0.001d;
        this.motionY = 0.05d;

        if (nbt.contains("FireworkItem")) {
            firework = NBTIO.getItemHelper(nbt.getCompound("FireworkItem"));
        } else {
            firework = new ItemFirework();
        }

        this.lifetime = firework instanceof ItemFirework ? ((ItemFirework) firework).getLifeTime() : 30;

        this.setDataProperty(new SlotEntityData(Entity.DATA_MINECART_DISPLAY_BLOCK, firework));
        this.setDataProperty(new IntEntityData(Entity.DATA_MINECART_DISPLAY_OFFSET, 1));
        this.setDataProperty(new ByteEntityData(Entity.DATA_MINECART_HAS_DISPLAY, 1));

    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }

        this.lastUpdate = currentTick;

        this.timing.startTiming();


        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (this.isAlive()) {

            this.motionX *= 1.15d;
            this.motionZ *= 1.15d;
            this.motionY += 0.04d;
            this.move(this.motionX, this.motionY, this.motionZ);

            this.updateMovement();


            float f = (float) Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.yaw = (float) (Math.atan2(this.motionX, this.motionZ) * (180d / Math.PI));

            this.pitch = (float) (Math.atan2(this.motionY, (double) f) * (180d / Math.PI));


            if (this.fireworkAge == 0) {
                this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_LAUNCH);
            }

            this.fireworkAge++;

            hasUpdate = true;
            if (this.fireworkAge >= this.lifetime) {
                EntityEventPacket pk = new EntityEventPacket();
                pk.event = EntityEventPacket.FIREWORK_PARTICLES;
                pk.entityRuntimeId = this.getId();

                level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_LARGE_BLAST, -1, NETWORK_ID);

                Server.broadcastPacket(getViewers().values(), pk);

                this.kill();
                hasUpdate = true;
            }
        }

        this.timing.stopTiming();

        return hasUpdate || !this.onGround || Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionY) > 0.00001 || Math.abs(this.motionZ) > 0.00001;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return (source.getCause() == DamageCause.VOID || source.getCause() == DamageCause.FIRE_TICK || source.getCause() == DamageCause.ENTITY_EXPLOSION || source.getCause() == DamageCause.BLOCK_EXPLOSION) && super.attack(source);
    }

    public void setFirework(Item item) {
        this.firework = item;
        this.setDataProperty(new SlotEntityData(Entity.DATA_MINECART_DISPLAY_BLOCK, item));
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }
}
