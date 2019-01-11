package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBanner;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;

public class BlockStandingBanner extends BlockTransparentMeta {

    public BlockStandingBanner() {
        this(0);
    }

    public BlockStandingBanner(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return STANDING_BANNER;
    }

    @Override
    public String getName() {
        return "Standing Banner";
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public double getResistance() {
        return 5;
    }

    @Override
    public double getHardness() {
        return 1;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return null;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (face != BlockFace.DOWN) {
            if (face == BlockFace.UP && player != null) {
                this.setDamage(NukkitMath.floorDouble(((player.yaw + 180) * 16 / 360) + 0.5) & 0x0f);
                this.getLevel().setBlock(block, this, true);
            } else {
                this.setDamage(face.getIndex());
                this.getLevel().setBlock(block, new BlockWallBanner(this.getDamage()), true);
            }

            CompoundTag nbt = new CompoundTag()
                    .putString("id", BlockEntity.BANNER)
                    .putInt("x", block.getFloorX())
                    .putInt("y", block.getFloorY())
                    .putInt("z", block.getFloorZ())
                    .put("Base", item.getNamedTag().contains("Base") ? item.getNamedTag().get("Base") : new IntTag("Base", item.getDamage() & 0x0f));

            if (item.getNamedTag().contains("Patterns") && item.getNamedTag().get("Patterns") instanceof ListTag) {
                nbt.put("Patterns", item.getNamedTag().get("Patterns"));
            }

            BlockEntity.createBlockEntity(BlockEntity.BANNER, this.level.getChunk(block.getFloorX() >> 4, block.getFloorZ() >> 4), nbt);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.down().getId() == AIR) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public Item toItem() {
        ItemBlock item = new ItemBlock(this, this.getDamage(), 1);
        BlockEntityBanner blockEntity = (BlockEntityBanner) this.getLevel().getBlockEntity(this);
        if (blockEntity != null) {
            item.setNamedTag(blockEntity.getCleanedNBT());
        }
        return item;
    }
}
