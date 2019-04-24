package cn.nukkit.entity.passive;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class WanderingTrader extends EntityVillager {

    public static final int NETWORK_ID = WANDERING_TRADER;

    public WanderingTrader(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public String getName() {
        return "Wandering Trader";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
}
