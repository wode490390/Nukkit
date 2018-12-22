package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2015/12/2 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockTorch extends BlockFlowable {

    public BlockTorch() {
        this(0);
    }

    public BlockTorch(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Torch";
    }

    @Override
    public int getId() {
        return TORCH;
    }

    @Override
    public int getLightLevel() {
        return 14;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            Block below = this.down();
            int side = this.getDamage();
            int[] faces = new int[]{
                    0, //0
                    4, //1
                    5, //2
                    2, //3
                    3, //4
                    0, //5
                    0  //6
            };
            Block face = this.getSide(BlockFace.fromIndex(faces[side]));

            if (face.isTransparent() && !(face instanceof BlockGlass || face instanceof BlockIce || face instanceof BlockIceFrosted) && !(side == 0 && (below instanceof BlockHopper || below instanceof BlockFence || below instanceof BlockWall))) {
                this.getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        Block below = this.down();

        if (face != BlockFace.DOWN && (!target.isTransparent() || target instanceof BlockGlass || target instanceof BlockIce || target instanceof BlockIceFrosted)) {
            int[] faces = new int[]{
                    0, //0, nerver used
                    5, //1
                    4, //2
                    3, //3
                    2, //4
                    1, //5
            };
            this.setDamage(faces[face.getIndex()]);
            this.getLevel().setBlock(block, this, true, true);

            return true;
        } else if (!below.isTransparent() || below instanceof BlockGlass || below instanceof BlockIce || below instanceof BlockIceFrosted || below instanceof BlockHopper || below instanceof BlockFence || below instanceof BlockWall) {
            this.setDamage(0);
            this.getLevel().setBlock(block, this, true, true);

            return true;
        }
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, 0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    public BlockFace getFacing() {
        return getFacing(this.getDamage());
    }

    public BlockFace getFacing(int meta) {
        switch (meta) {
            case 1:
                return BlockFace.EAST;
            case 2:
                return BlockFace.WEST;
            case 3:
                return BlockFace.SOUTH;
            case 4:
                return BlockFace.NORTH;
            default:
                return BlockFace.UP;
        }
    }
}
