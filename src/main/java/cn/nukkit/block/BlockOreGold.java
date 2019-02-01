package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockOreGold extends BlockOre {

    public BlockOreGold() {

    }

    @Override
    public int getId() {
        return GOLD_ORE;
    }

    @Override
    public String getName() {
        return "Gold Ore";
    }

    @Override
    public int getToolHarvestLevel() {
        return ItemTool.TIER_IRON;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolHarvestLevel()) {
            return new Item[]{
                    Item.get(GOLD_ORE)
            };
        } else {
            return new Item[0];
        }
    }
}
