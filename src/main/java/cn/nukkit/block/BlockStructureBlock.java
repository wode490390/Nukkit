package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;

public class BlockStructureBlock extends BlockSolidMeta {

    public static final int MODE_INVENTORY_MODEL = 0;
    public static final int MODE_DATA = 1;
    public static final int MODE_SAVE = 2;
    public static final int MODE_LOAD = 3;
    public static final int MODE_CORNER = 4;
    public static final int MODE_EXPORT = 5;

    public BlockStructureBlock() {
        this(0);
    }

    public BlockStructureBlock(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return STRUCTURE_BLOCK;
    }

    @Override
    public String getName() {
        return "Structure Block";
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public double getResistance() {
        return 6000000;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(get(AIR));
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setDamage(MODE_EXPORT);
        this.level.setBlock(block, this, true, false);

        return true;
    }
}
