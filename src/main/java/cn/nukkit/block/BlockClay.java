package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @author Nukkit Project Team
 */
public class BlockClay extends BlockSolid {

    public BlockClay() {

    }

    @Override
    public double getHardness() {
        return 0.6000000238418579;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public int getId() {
        return CLAY_BLOCK;
    }

    @Override
    public String getName() {
        return "Clay Block";
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{
                Item.get(Item.CLAY, 0, 4)
        };
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLAY_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
