package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by Snake1999 on 2016/2/4.
 * Package cn.nukkit.blockentity in project Nukkit.
 */
public class BlockEntityFlowerPot extends BlockEntitySpawnable {

    public BlockEntityFlowerPot(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("item")) {
            this.namedTag.putShort("item", 0);
        }

        if (!this.namedTag.contains("data")) {
            if (this.namedTag.contains("mData")) {
                this.namedTag.putInt("data", this.namedTag.getInt("mData"));
                this.namedTag.remove("mData");
            } else {
                this.namedTag.putInt("data", 0);
            }
        }

        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.getBlock().getId() == Block.FLOWER_POT_BLOCK;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return getDefaultCompound(this, FLOWER_POT)
                .putShort("item", this.namedTag.getShort("item"))
                .putInt("mData", this.namedTag.getInt("data"));
    }
}
