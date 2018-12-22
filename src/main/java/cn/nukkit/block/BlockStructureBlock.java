package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;

public class BlockStructureBlock extends BlockSolidMeta {

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
        return 18000000;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockAir());
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
        this.setDamage(5);
        this.level.setBlock(block, this, true, false);

        return true;
    }
}
