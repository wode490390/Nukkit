package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockOreIron extends BlockOre {

    public BlockOreIron() {

    }

    @Override
    public int getId() {
        return IRON_ORE;
    }

    @Override
    public String getName() {
        return "Iron Ore";
    }

    @Override
    public int getToolHarvestLevel() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolHarvestLevel()) {
            return new Item[]{
                    Item.get(IRON_ORE)
            };
        } else {
            return new Item[0];
        }
    }
}
