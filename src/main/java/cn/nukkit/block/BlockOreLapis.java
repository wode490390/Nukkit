package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockOreLapis extends BlockOre {

    public BlockOreLapis() {

    }

    @Override
    public int getId() {
        return LAPIS_ORE;
    }

    @Override
    public int getToolHarvestLevel() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public String getName() {
        return "Lapis Lazuli Ore";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolHarvestLevel()) {
            int count = 4 + ThreadLocalRandom.current().nextInt(5);
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int i = ThreadLocalRandom.current().nextInt(fortune.getLevel() + 2) - 1;

                if (i < 0) {
                    i = 0;
                }

                count *= (i + 1);
            }

            return new Item[]{
                    Item.get(Item.DYE, ItemDye.LIGHT_BLUE, ThreadLocalRandom.current().nextInt(4, 8))
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
