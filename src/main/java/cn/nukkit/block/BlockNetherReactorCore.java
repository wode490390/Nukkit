package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDiamond;
import cn.nukkit.item.ItemIngotIron;
import cn.nukkit.item.ItemTool;

public class BlockNetherReactorCore extends BlockSolidMeta {

    public static final int STATE_INACTIVE = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_USED = 2;

	public BlockNetherReactorCore() {
        this(0);
	}

	public BlockNetherReactorCore(int meta) {
        super(meta);
	}

    @Override
    public String getName() {
        String[] names = new String[]{
                "Nether Reactor Core",
                "Initialized Nether Reactor Core",
                "Finished Nether Reactor Core"
        };

        return names[this.getDamage() > 2 ? 0 : this.getDamage()];
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
            return new Item[]{new ItemDiamond(0, 3), new ItemIngotIron(0, 6)};
        } else return new Item[]{};
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
