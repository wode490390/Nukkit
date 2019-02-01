package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 2015/12/26 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockOreQuartz extends BlockOre {

    public BlockOreQuartz() {

    }

    @Override
    public String getName() {
        return "Quartz Ore";
    }

    @Override
    public int getId() {
        return QUARTZ_ORE;
    }

    @Override
    public int getToolHarvestLevel() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolHarvestLevel()) {
            int count = 1;
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int i = ThreadLocalRandom.current().nextInt(fortune.getLevel() + 2) - 1;

                if (i < 0) {
                    i = 0;
                }

                count = i + 1;
            }

            return new Item[]{
                    Item.get(Item.QUARTZ, 0, count)
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public int getDropExp() {
        return ThreadLocalRandom.current().nextInt(2, 5);
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
