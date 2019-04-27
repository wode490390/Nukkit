package cn.nukkit.entity.mob;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author PikyCZ
 */
public class EntityWitherSkeleton extends EntityMob {

    public static final int NETWORK_ID = WITHER_SKELETON;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityWitherSkeleton(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(20);
    }

    @Override
    public float getWidth() {
        return 0.72f;
    }

    @Override
    public float getHeight() {
        return 2.01f;
    }

    @Override
    public String getName() {
        return "Wither Skeleton";
    }
}
