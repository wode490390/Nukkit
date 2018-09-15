package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pub4Game on 15.01.2016.
 */
public class BlockVine extends BlockTransparent {

    private static final int FLAG_SOUTH = 0x01;
    private static final int FLAG_WEST = 0x02;
    private static final int FLAG_NORTH = 0x04;
    private static final int FLAG_EAST = 0x08;

    public BlockVine(int meta) {
        super(meta);
    }

    public BlockVine() {
        this(0);
    }

    @Override
    public String getName() {
        return "Vines";
    }

    @Override
    public int getId() {
        return VINE;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public double getResistance() {
        return 1;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public boolean canBeClimbed() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
        entity.onGround = true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        double f1 = 1;
        double f2 = 1;
        double f3 = 1;
        double f4 = 0;
        double f5 = 0;
        double f6 = 0;
        boolean flag = this.meta > 0;
        if ((this.meta & 0x02) > 0) {
            f4 = Math.max(f4, 0.0625);
            f1 = 0;
            f2 = 0;
            f5 = 1;
            f3 = 0;
            f6 = 1;
            flag = true;
        }
        if ((this.meta & 0x08) > 0) {
            f1 = Math.min(f1, 0.9375);
            f4 = 1;
            f2 = 0;
            f5 = 1;
            f3 = 0;
            f6 = 1;
            flag = true;
        }
        if ((this.meta & 0x01) > 0) {
            f3 = Math.min(f3, 0.9375);
            f6 = 1;
            f1 = 0;
            f4 = 1;
            f2 = 0;
            f5 = 1;
            flag = true;
        }
        if (!flag && this.up().isSolid()) {
            f2 = Math.min(f2, 0.9375);
            f5 = 1;
            f1 = 0;
            f4 = 1;
            f3 = 0;
            f6 = 1;
        }
        return new AxisAlignedBB(
                this.x + f1,
                this.y + f2,
                this.z + f3,
                this.x + f4,
                this.y + f5,
                this.z + f6
        );
    }

    private static final Map<Integer, BlockFace> faces = new HashMap<Integer, BlockFace>(){{
        put(FLAG_SOUTH, BlockFace.SOUTH);
        put(FLAG_WEST, BlockFace.WEST);
        put(FLAG_NORTH, BlockFace.NORTH);
        put(FLAG_EAST, BlockFace.EAST);
    }};

    private static final Map<BlockFace, Integer> facesPlace = new HashMap<BlockFace, Integer>(){{
        put(BlockFace.SOUTH, FLAG_SOUTH);
        put(BlockFace.WEST, FLAG_WEST);
        put(BlockFace.NORTH, FLAG_NORTH);
        put(BlockFace.EAST, FLAG_EAST);
    }};

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if(!target.isSolid() || face == BlockFace.UP || face == BlockFace.DOWN){
            return false;
        }
        this.meta = facesPlace.getOrDefault(face, 0);
        if (block.getId() == this.getId()) {
            this.meta |= block.meta;
        }
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShears()) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, 0);
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            int[] meta = new int[]{this.meta};
            faces.forEach((flag, side) -> {
                if ((meta[0] & flag) != 0){
                    if(!this.getSide(side).isSolid()){
                        meta[0] &= ~flag;
                    }
                }
            });
            if (meta[0] != this.meta) {
                if (meta[0] == 0) {
                    this.level.useBreakOn(this);
                } else {
                    this.meta = meta[0];
                    this.level.setBlock(this, this);
                }
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHEARS;
    }
}
