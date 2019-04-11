package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;

/**
 * Created by CreeperFace on 26. 11. 2016.
 */
public class BlockSlabRedSandstone extends BlockSlab {

    public static final int TYPE_RED_SANDSTONE = 0;
    public static final int TYPE_PURPUR = 1;
    public static final int TYPE_PRISMARINE = 2;
    public static final int TYPE_DARK_PRISMARINE = 3;
    public static final int TYPE_PRISMARINE_BRICKS = 4;
    public static final int TYPE_MOSSY_COBBLESTONE = 5;
    public static final int TYPE_SMOOTH_SANDSTONE = 6;
    public static final int TYPE_RED_NETHER_BRICK = 7;

    public BlockSlabRedSandstone() {
        this(0);
    }

    public BlockSlabRedSandstone(int meta) {
        super(meta, DOUBLE_RED_SANDSTONE_SLAB);
    }

    @Override
    public int getId() {
        return RED_SANDSTONE_SLAB;
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Red Sandstone",
                "Purpur",
                "Prismarine",
                "Dark Prismarine",
                "Prismarine Bricks",
                "Mossy Cobblestone",
                "Smooth Sandstone",
                "Red Nether Brick"
        };

        return ((this.getDamage() & 0x8) > 0 ? "Upper " : "") + names[this.getDamage() & 0x7] + " Slab";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolHarvestLevel()) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, this.getDamage() & 0x7);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolHarvestLevel() {
        return ItemTool.TIER_WOODEN;
    }
}
