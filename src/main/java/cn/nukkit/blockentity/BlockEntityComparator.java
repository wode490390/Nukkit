package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author CreeperFace
 */
public class BlockEntityComparator extends BlockEntity {

    private int outputSignal;

    public BlockEntityComparator(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        if (!nbt.contains("OutputSignal")) {
            nbt.putInt("OutputSignal", 0);
        }

        this.outputSignal = nbt.getInt("OutputSignal");
    }

    @Override
    public boolean isBlockEntityValid() {
        int blockId = this.getBlock().getId();
        return blockId == Block.UNPOWERED_COMPARATOR || blockId == Block.POWERED_COMPARATOR;
    }

    public int getOutputSignal() {
        return outputSignal;
    }

    public void setOutputSignal(int outputSignal) {
        this.outputSignal = outputSignal;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("OutputSignal", this.outputSignal);
    }
}
