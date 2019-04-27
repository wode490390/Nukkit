package cn.nukkit.entity.mob;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author PikyCZ
 */
public class EntityMagmaCube extends EntityMob {

    public static final int NETWORK_ID = MAGMA_CUBE;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityMagmaCube(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(16);
    }

    @Override
    public float getWidth() {
        return 2.08f;
    }

    @Override
    public float getHeight() {
        return 2.08f;
    }

    @Override
    public String getName() {
        return "Magma Cube";
    }
}
