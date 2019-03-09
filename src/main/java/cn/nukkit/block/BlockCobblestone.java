package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockCobblestone extends BlockSolid {

    public BlockCobblestone() {

    }

    @Override
    public int getId() {
        return COBBLESTONE;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolHarvestLevel() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public String getName() {
        return "Cobblestone";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolHarvestLevel()) {
            return new Item[]{
                    this.toItem()
            };
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
