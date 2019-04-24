package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBed;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBed;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockBed extends BlockTransparentMeta implements BlockFaceable {

    public BlockBed() {
        this(0);
    }

    public BlockBed(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BED_BLOCK;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.20000000298023224;
    }

    @Override
    public String getName() {
        return this.getDyeColor().getName() + " Bed Block";
    }

    @Override
    public double getMaxY() {
        return this.y + 0.5625;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        int time = this.getLevel().getTime() % Level.TIME_FULL;

        boolean isNight = (time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE);

        if (player != null && !isNight) {
            player.sendMessage(new TranslationContainer("tile.bed.noSleep"));
            return true;
        }

        Block blockNorth = this.north();
        Block blockSouth = this.south();
        Block blockEast = this.east();
        Block blockWest = this.west();

        Block b;
        if ((this.getDamage() & 0x8) == 0x8) {
            b = this;
        } else {
            if (blockNorth.getId() == this.getId() && (blockNorth.getDamage() & 0x8) == 0x8) {
                b = blockNorth;
            } else if (blockSouth.getId() == this.getId() && (blockSouth.getDamage() & 0x8) == 0x8) {
                b = blockSouth;
            } else if (blockEast.getId() == this.getId() && (blockEast.getDamage() & 0x8) == 0x8) {
                b = blockEast;
            } else if (blockWest.getId() == this.getId() && (blockWest.getDamage() & 0x8) == 0x8) {
                b = blockWest;
            } else {
                if (player != null) {
                    player.sendMessage(new TranslationContainer("tile.bed.notValid"));
                }

                return true;
            }
        }

        if (player != null && !player.sleepOn(b)) {
            player.sendMessage(new TranslationContainer("tile.bed.occupied"));
        }


        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        Block down = this.down();
        if (!down.isTransparent()) {
            Block next = this.getSide(player.getDirection());
            Block downNext = next.down();

            if (next.canBeReplaced() && !downNext.isTransparent()) {
                int meta = player.getDirection().getHorizontalIndex();

                this.getLevel().setBlock(block, Block.get(this.getId(), meta), true);
                this.getLevel().setBlock(next, Block.get(this.getId(), meta | 0x8), true);

                this.createBlockEntity(this, item.getDamage());
                this.createBlockEntity(next, item.getDamage());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        Block blockNorth = this.north(); //Gets the blocks around them
        Block blockSouth = this.south();
        Block blockEast = this.east();
        Block blockWest = this.west();

        if ((this.getDamage() & 0x8) == 0x8) { //This is the Top part of bed
            if (blockNorth.getId() == this.getId() && blockNorth.getDamage() != 0x8) { //Checks if the block ID&&meta are right
                this.getLevel().setBlock(blockNorth, new BlockAir(), true, true);
            } else if (blockSouth.getId() == this.getId() && blockSouth.getDamage() != 0x8) {
                this.getLevel().setBlock(blockSouth, new BlockAir(), true, true);
            } else if (blockEast.getId() == this.getId() && blockEast.getDamage() != 0x8) {
                this.getLevel().setBlock(blockEast, new BlockAir(), true, true);
            } else if (blockWest.getId() == this.getId() && blockWest.getDamage() != 0x8) {
                this.getLevel().setBlock(blockWest, new BlockAir(), true, true);
            }
        } else { //Bottom Part of Bed
            if (blockNorth.getId() == this.getId() && (blockNorth.getDamage() & 0x8) == 0x8) {
                this.getLevel().setBlock(blockNorth, new BlockAir(), true, true);
            } else if (blockSouth.getId() == this.getId() && (blockSouth.getDamage() & 0x8) == 0x8) {
                this.getLevel().setBlock(blockSouth, new BlockAir(), true, true);
            } else if (blockEast.getId() == this.getId() && (blockEast.getDamage() & 0x8) == 0x8) {
                this.getLevel().setBlock(blockEast, new BlockAir(), true, true);
            } else if (blockWest.getId() == this.getId() && (blockWest.getDamage() & 0x8) == 0x8) {
                this.getLevel().setBlock(blockWest, new BlockAir(), true, true);
            }
        }
        this.getLevel().setBlock(this, new BlockAir(), true, true);

        return true;
    }

    private void createBlockEntity(Vector3 pos, int color) {
        new BlockEntityBed(this.getLevel().getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4), BlockEntity.getDefaultCompound(this, BlockEntity.BED).putByte("color", color));
    }

    @Override
    public Item toItem() {
        return new ItemBed(this.getDyeColor().getWoolData());
    }

    @Override
    public BlockColor getColor() {
        return this.getDyeColor().getColor();
    }

    public DyeColor getDyeColor() {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (blockEntity instanceof BlockEntityBed) {
            return ((BlockEntityBed) blockEntity).getDyeColor();
        }
        this.createBlockEntity(this, DyeColor.RED.getWoolData());
        return DyeColor.RED;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }
}
