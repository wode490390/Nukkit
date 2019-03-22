package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;

/**
 * Created by CreeperFace on 2.6.2017.
 */
public class BlockEntityBed extends BlockEntitySpawnable {

    public BlockEntityBed(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("color")) {
            this.namedTag.putByte("color", 14);
        }

        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.getFloorX(), this.getFloorY(), this.getFloorZ()) == Block.BED_BLOCK;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return getDefaultCompound(this, BED).putByte("color", this.getColor());
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(this.getColor());
    }

    public int getColor() {
        return this.namedTag.getByte("color", 14);
    }

    public void setColor(int color) {
        this.namedTag.putByte("color", color & 0xf);
        this.setDirty();
    }
}
