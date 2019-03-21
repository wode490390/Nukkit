package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockEntityMusic extends BlockEntity implements BlockEntityPowerable {

    private int note = 0;
    private boolean powered = false;

    public BlockEntityMusic(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        if (this.namedTag.contains("note")) {
            this.note = this.namedTag.getByte("note");
        }
        if (this.namedTag.contains("powered")) {
            this.powered = this.namedTag.getBoolean("powered");
        }

        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.getLevel().getBlockIdAt(this.getFloorX(), this.getFloorY(), this.getFloorZ()) == Block.NOTEBLOCK;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("note", this.note);
        this.namedTag.putBoolean("powered", this.powered);
    }

    @Override
    public void setPowered() {
        this.setPowered(true);
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered =  powered;
    }

    @Override
    public boolean isPowered() {
        return this.powered;
    }

    public void changePitch() {
        this.note = (this.note + 1) % 25;
    }

    public int getPitch() {
        return this.note;
    }
}
