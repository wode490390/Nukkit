package cn.nukkit.entity.mob;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author PikyCZ
 */
public class EntityWither extends EntityMob {

    public static final int NETWORK_ID = WITHER;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityWither(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 1;
    }

    @Override
    public float getHeight() {
        return 3;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(600);
    }

    @Override
    public String getName() {
        return "Wither";
    }
}
