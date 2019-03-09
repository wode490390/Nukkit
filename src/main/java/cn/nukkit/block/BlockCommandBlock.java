package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;

public class BlockCommandBlock extends BlockSolidMeta {

    public BlockCommandBlock() {
        this(0);
    }

    public BlockCommandBlock(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return COMMAND_BLOCK;
    }

    @Override
    public String getName() {
        return "Impulse Command Block";
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
        int faces[] = {2, 5, 3, 4};
        if (player != null) {
            if (Math.abs(player.x - this.x) < 2 && Math.abs(player.z - this.z) < 2) {
                double y = player.y + player.getEyeHeight();
                if (y - this.y > 2) {
                    this.setDamage(BlockFace.UP.getIndex());
                } else if (this.y - y > 0) {
                    this.setDamage(BlockFace.DOWN.getIndex());
                } else {
                    this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
                }
            } else {
                this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
            }
        } else {
            this.setDamage(0);
        }
        this.level.setBlock(block, this, true, false);

        return true;
    }
}
