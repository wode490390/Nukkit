package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockMovingBlock extends BlockTransparent {

    public BlockMovingBlock() {

    }

    @Override
    public int getId() {
        return MOVING_BLOCK;
    }

    @Override
    public String getName() {
        return "Moving Block";
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockAir());
    }
}
