package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

public class BlockNetherReactorCore extends BlockSolid {

    public BlockNetherReactorCore() {

    }

    @Override
    public String getName() {
        return "Nether Reactor Core";
    }

    @Override
    public int getId() {
        return NETHER_REACTOR_CORE;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new Item[]{
                    Item.get(Item.DIAMOND, 0, 3),
                    Item.get(Item.IRON_INGOT, 0, 6)
            };
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
