package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockWall extends BlockTransparentMeta {

    public static final int NONE_MOSSY_WALL = 0;
    public static final int MOSSY_WALL = 1;
    public static final int GRANITE_WALL = 2;
    public static final int DIORITE_WALL = 3;
    public static final int ANDESITE_WALL = 4;
    public static final int SANDSTONE_WALL = 5;
    public static final int BRICK_WALL = 6;
    public static final int STONE_BRICK_WALL = 7;
    public static final int MOSSY_STONE_BRICK_WALL = 8;
    public static final int NETHER_BRICK_WALL = 9;
    public static final int END_STONE_BRICK_WALL = 10;
    public static final int PRISMARINE_WALL = 11;
    public static final int RED_SANDSTONE_WALL = 12;
    public static final int RED_NETHER_BRICK_WALL = 13;

    public BlockWall() {
        this(0);
    }

    public BlockWall(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return STONE_WALL;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Cobblestone",
                "Mossy Cobblestone",
                "Granite",
                "Diorite",
                "Andesite",
                "Sandstone",
                "Brick",
                "Stone Brick",
                "Mossy Stone Brick",
                "End Stone Brick",
                "Nether Brick",
                "Prismarine",
                "Red Sandstone",
                "Red Nether Brick",
                "",
                ""
        };
        return names[this.getDamage() & 0xf] + " Wall";
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {

        boolean north = this.canConnect(this.getSide(BlockFace.NORTH));
        boolean south = this.canConnect(this.getSide(BlockFace.SOUTH));
        boolean west = this.canConnect(this.getSide(BlockFace.WEST));
        boolean east = this.canConnect(this.getSide(BlockFace.EAST));

        double n = north ? 0 : 0.25;
        double s = south ? 1 : 0.75;
        double w = west ? 0 : 0.25;
        double e = east ? 1 : 0.75;

        if (north && south && !west && !east) {
            w = 0.3125;
            e = 0.6875;
        } else if (!north && !south && west && east) {
            n = 0.3125;
            s = 0.6875;
        }

        return new SimpleAxisAlignedBB(
                this.x + w,
                this.y,
                this.z + n,
                this.x + e,
                this.y + 1.5,
                this.z + s
        );
    }

    public boolean canConnect(Block block) {
        return (!(block.getId() != COBBLE_WALL && block.getId() != FENCE_GATE)) || block.isSolid() && !block.isTransparent();
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolHarvestLevel() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
