package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by Pub4Game on 21.06.2016.
 */
public class EntityNPC extends EntityCreature {

    public static final int NETWORK_ID = NPC;

    public EntityNPC(FullChunk chunk, CompoundTag nbt) {
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
        return 2.1f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(20);
    }

    @Override
    public String getName() {
        return "NPC";
    }

    @Override
    public boolean isEducation() {
        return true;
    }
}
