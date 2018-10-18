package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockRedstoneTorch extends BlockTorch {

    public BlockRedstoneTorch() {
        this(0);
    }

    public BlockRedstoneTorch(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Redstone Torch";
    }

    @Override
    public int getId() {
        return REDSTONE_TORCH;
    }

    @Override
    public int getLightLevel() {
        return 7;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (!super.place(item, block, target, face, fx, fy, fz, player)) {
            return false;
        }

        if (!checkState()) {
            BlockFace facing = getFacing().getOpposite();
            Vector3 pos = getLocation();

            for (BlockFace side : BlockFace.values()) {
                if (facing == side) {
                    continue;
                }

                this.level.updateAround(pos.getSide(side));
            }
        }

        return true;
    }

    @Override
    public int getWeakPower(BlockFace side) {
        //return BlockFace.getFront(this.meta).getOpposite() != side ? 15 : 0;
        return 15;
    }

    @Override
    public int getStrongPower(BlockFace side) {
        return side == BlockFace.DOWN ? this.getWeakPower(side) : 0;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockAir(), true, true);
        Vector3 pos = getLocation();

        for (BlockFace side : BlockFace.values()) {
            this.level.updateAroundRedstone(pos.getSide(side), null);
        }
        return true;
    }

    protected boolean checkState() {
        BlockFace face = getFacing().getOpposite();
        Vector3 pos = getLocation();

        if (this.level.isSidePowered(pos.getSide(face), face)) {
            this.level.setBlock(pos, new BlockRedstoneTorchUnlit(getDamage()), false, true);

            for (BlockFace side : BlockFace.values()) {
                if (side == face) {
                    continue;
                }

                this.level.updateAroundRedstone(pos.getSide(side), null);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }
}
