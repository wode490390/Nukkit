package cn.nukkit.entity.item;

import cn.nukkit.block.BlockTNT;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.item.ItemMinecartTNT;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.MinecartType;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * Author: Adam Matthew [larryTheCoder]
 * <p>
 * Nukkit Project.
 */
public class EntityMinecartTNT extends EntityMinecartAbstract implements EntityExplosive {

    public static final int NETWORK_ID = TNT_MINECART;

    private int fuse;
    private boolean activated = false;

    public EntityMinecartTNT(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        super.setDisplayBlock(new BlockTNT(), false);
    }

    @Override
    public boolean isRideable() {
        return false;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        if (namedTag.contains("TNTFuse")) {
            fuse = namedTag.getByte("TNTFuse");
        } else {
            fuse = 80;
        }

        this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHARGED, false);
        this.setDataProperty(new IntEntityData(DATA_FUSE_LENGTH, fuse));
    }

    @Override
    public boolean onUpdate(int currentTick) {
        this.timing.startTiming();

        // Todo: Check why the TNT doesn't want to tick
        if (activated || fuse < 80) {
            int tickDiff = currentTick - lastUpdate;

            lastUpdate = currentTick;

            if (fuse % 5 == 0) {
                setDataProperty(new IntEntityData(DATA_FUSE_LENGTH, fuse));
            }

            fuse -= tickDiff;

            if (isAlive() && fuse <= 0) {
                if (level.getGameRules().getBoolean(GameRule.TNT_EXPLODES)) {
                    explode(ThreadLocalRandom.current().nextInt(5));
                }
                kill();
            }

            log.info("Debug:" + fuse);
        }

        this.timing.stopTiming();

        return super.onUpdate(currentTick);
    }

    @Override
    public void activate(int x, int y, int z, boolean flag) {

    }

    @Override
    public void explode() {
        explode(0);
    }

    public void explode(double square) {
        double root = Math.sqrt(square);

        if (root > 5.0D) {
            root = 5.0D;
        }

        EntityExplosionPrimeEvent event = new EntityExplosionPrimeEvent(this, (4.0D + ThreadLocalRandom.current().nextDouble() * 1.5D * root));
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        Explosion explosion = new Explosion(this, event.getForce(), this);
        if (event.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
        kill();
    }

    @Override
    public void dropItem() {
        level.dropItem(this, new ItemMinecartTNT());
    }

    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(3);
    }

    @Override
    public int getNetworkId() {
        return EntityMinecartTNT.NETWORK_ID;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        super.namedTag.putInt("TNTFuse", this.fuse);
    }
}
