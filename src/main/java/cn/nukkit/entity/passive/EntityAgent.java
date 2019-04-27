package cn.nukkit.entity.passive;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityAgent extends Entity {

    public static final int NETWORK_ID = AGENT;

    public EntityAgent(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 0.93f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
    }

    @Override
    public boolean isEducation() {
        return true;
    }
}
