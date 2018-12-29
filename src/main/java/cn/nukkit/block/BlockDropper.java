package cn.nukkit.block;

import cn.nukkit.Player;
//import cn.nukkit.blockentity.BlockEntity;
//import cn.nukkit.blockentity.BlockEntityDropper;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

public class BlockDropper extends BlockSolidMeta {

    public BlockDropper() {
        this(0);
    }

    public BlockDropper(int meta) {
        super(meta);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public int getId() {
        return DROPPER;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, 0);
    }

    @Override
    public int getComparatorInputOverride() {
        /*BlockEntity blockEntity = this.level.getBlockEntity(this);
        if(blockEntity instanceof BlockEntityDropper) {
            //return ContainerInventory.calculateRedstone(((BlockEntityDropper) blockEntity).getInventory()); TODO: dropper
        }*/

        return super.getComparatorInputOverride();
    }

    public BlockFace getFacing() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    public boolean isTriggered() {
        return (this.getDamage() & 8) > 0;
    }

    public void setTriggered(boolean value) {
        int i = 0;
        i |= getFacing().getIndex();

        if (value) {
            i |= 8;
        }

        this.setDamage(i);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    public Vector3 getDropperPosition() {
        BlockFace facing = getFacing();
        double x = this.getX() + 0.7 * facing.getXOffset();
        double y = this.getY() + 0.7 * facing.getYOffset();
        double z = this.getZ() + 0.7 * facing.getZOffset();
        return new Vector3(x, y, z);
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
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

    /*@Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity t = this.getLevel().getBlockEntity(this);
            BlockEntityDropper container;
            if (t instanceof BlockEntityDropper) {
                container = (BlockEntityDropper) t;
            } else {
                CompoundTag nbt = new CompoundTag("")
                        .putList(new ListTag<>("Items"))
                        .putString("id", BlockEntity.DROPPER)
                        .putInt("x", (int) this.x)
                        .putInt("y", (int) this.y)
                        .putInt("z", (int) this.z);
                container = new BlockEntityDropper(this.getLevel().getChunk((int) (this.x) >> 4, (int) (this.z) >> 4), nbt);
            }

            player.addWindow(container.getInventory());
        }

        return true;
    }*/
}
