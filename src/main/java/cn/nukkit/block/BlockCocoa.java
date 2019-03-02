package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.utils.DyeColor;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by CreeperFace on 27. 10. 2016.
 */
public class BlockCocoa extends BlockTransparentMeta implements BlockFaceable {

    protected static final AxisAlignedBB[] EAST = new SimpleAxisAlignedBB[]{new SimpleAxisAlignedBB(0.6875, 0.4375, 0.375, 0.9375, 0.75, 0.625), new SimpleAxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875), new SimpleAxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875)};
    protected static final AxisAlignedBB[] WEST = new SimpleAxisAlignedBB[]{new SimpleAxisAlignedBB(0.0625, 0.4375, 0.375, 0.3125, 0.75, 0.625), new SimpleAxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875), new SimpleAxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875)};
    protected static final AxisAlignedBB[] NORTH = new SimpleAxisAlignedBB[]{new SimpleAxisAlignedBB(0.375, 0.4375, 0.0625, 0.625, 0.75, 0.3125), new SimpleAxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375), new SimpleAxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375)};
    protected static final AxisAlignedBB[] SOUTH = new SimpleAxisAlignedBB[]{new SimpleAxisAlignedBB(0.375, 0.4375, 0.6875, 0.625, 0.75, 0.9375), new SimpleAxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375), new SimpleAxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375)};
    protected static final AxisAlignedBB[] ALL = new AxisAlignedBB[12];

    public BlockCocoa() {
        this(0);
    }

    public BlockCocoa(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return COCOA;
    }

    @Override
    public String getName() {
        return "Cocoa";
    }

    @Override
    public double getMinX() {
        return this.x + getRelativeBoundingBox().getMinX();
    }

    @Override
    public double getMaxX() {
        return this.x + getRelativeBoundingBox().getMaxX();
    }

    @Override
    public double getMinY() {
        return this.y + getRelativeBoundingBox().getMinY();
    }

    @Override
    public double getMaxY() {
        return this.y + getRelativeBoundingBox().getMaxY();
    }

    @Override
    public double getMinZ() {
        return this.z + getRelativeBoundingBox().getMinZ();
    }

    @Override
    public double getMaxZ() {
        return this.z + getRelativeBoundingBox().getMaxZ();
    }

    private AxisAlignedBB getRelativeBoundingBox() {
        int damage = this.getDamage();
        if (damage > 11) {
            this.setDamage(damage = 11);
        }
        AxisAlignedBB boundingBox = ALL[damage];
        if (boundingBox != null) return boundingBox;

        AxisAlignedBB[] bbs;

        switch (getDamage()) {
            case 0:
            case 4:
            case 8:
                bbs = NORTH;
                break;
            case 1:
            case 5:
            case 9:
                bbs = EAST;
                break;
            case 2:
            case 6:
            case 10:
                bbs = SOUTH;
                break;
            case 3:
            case 7:
            case 11:
                bbs = WEST;
                break;
            default:
                bbs = NORTH;
                break;
        }

        return ALL[damage] = bbs[this.getDamage() >> 2];
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        return this.place(item, block, target, face, fx, fy, fz, null);
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (target.getId() == Block.WOOD && (target.getDamage() & 0x3) == BlockWood.JUNGLE) {
            if (face != BlockFace.DOWN && face != BlockFace.UP) {
                int[] faces = new int[]{
                        0,
                        0,
                        0,
                        2,
                        3,
                        1,
                };

                this.setDamage(faces[face.getIndex()]);
                this.level.setBlock(block, this, true, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            int[] faces = new int[]{
                    3, 4, 2, 5, 3, 4, 2, 5, 3, 4, 2, 5
            };

            Block side = this.getSide(BlockFace.fromIndex(faces[this.getDamage()]));

            if (side.getId() != Block.WOOD && (side.getDamage() & 0x3) != BlockWood.JUNGLE) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (ThreadLocalRandom.current().nextInt(2) == 1) {
                if (this.getDamage() / 4 < 2) {
                    BlockCocoa block = (BlockCocoa) this.clone();
                    block.setDamage(block.getDamage() + 4);
                    BlockGrowEvent ev = new BlockGrowEvent(this, block);
                    Server.getInstance().getPluginManager().callEvent(ev);

                    if (!ev.isCancelled()) {
                        this.getLevel().setBlock(this, ev.getNewState(), true, true);
                    } else {
                        return Level.BLOCK_UPDATE_RANDOM;
                    }
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM;
            }
        }

        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == Item.DYE && item.getDamage() == 0xf) {
            Block block = this.clone();
            if (this.getDamage() / 4 < 2) {
                block.setDamage(block.getDamage() + 4);
                BlockGrowEvent ev = new BlockGrowEvent(this, block);
                Server.getInstance().getPluginManager().callEvent(ev);

                if (ev.isCancelled()) {
                    return false;
                }
                this.getLevel().setBlock(this, ev.getNewState(), true, true);
            }

            this.level.addParticle(new BoneMealParticle(this.add(0.5, 0.5, 0.5)));
            item.count--;
            return true;
        }

        return false;
    }

    @Override
    public double getResistance() {
        return 5;
    }

    @Override
    public double getHardness() {
        return 0.20000000298023224;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public Item toItem() {
        return Item.get(Item.DYE, DyeColor.BROWN.getDyeData());
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() >= 8) {
            return new Item[]{
                    Item.get(Item.DYE, DyeColor.BROWN.getDyeData(), 3)
            };
        }
        return new Item[]{
                this.toItem()
        };
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }
}
