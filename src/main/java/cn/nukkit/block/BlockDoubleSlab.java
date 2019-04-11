package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class BlockDoubleSlab extends BlockSolidMeta {

    protected BlockDoubleSlab(int meta) {
        super(meta);
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return this.getToolType() < ItemTool.TYPE_AXE ? 10 : 5;
    }
}
