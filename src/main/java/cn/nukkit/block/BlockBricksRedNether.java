package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockBricksRedNether extends BlockNetherBrick {

    public BlockBricksRedNether() {

    }

    @Override
    public String getName() {
        return "Red Nether Bricks";
    }

    @Override
    public int getId() {
        return RED_NETHER_BRICK;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolHarvestLevel()) {
            return new Item[]{
                    Item.get(Item.RED_NETHER_BRICK)
            };
        }
        return new Item[0];
    }
}
