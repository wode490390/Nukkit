package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class BlockItemFrame extends BlockTransparentMeta {

    public BlockItemFrame() {
        this(0);
    }

    public BlockItemFrame(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return ITEM_FRAME_BLOCK;
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!this.getSide(this.getFacing()).isSolid()) {
                this.level.useBreakOn(this);
                return type;
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
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (blockEntity instanceof BlockEntityItemFrame) {
            BlockEntityItemFrame itemFrame = (BlockEntityItemFrame) blockEntity;
            Item itemOnFrame = itemFrame.getItem();
            if (itemOnFrame == null || itemOnFrame.isNull()) {
                itemOnFrame = item.clone();
                if (player != null && player.isSurvival()) {
                    itemOnFrame.setCount(itemOnFrame.getCount() - 1);
                    player.getInventory().setItemInHand(itemOnFrame);
                }
                itemOnFrame.setCount(1);
                itemFrame.setItem(itemOnFrame);
                this.getLevel().addSound(this, Sound.BLOCK_ITEMFRAME_ADD_ITEM);
            } else {
                itemFrame.setItemRotation((itemFrame.getItemRotation() + 1) % 8);
                this.getLevel().addSound(this, Sound.BLOCK_ITEMFRAME_ROTATE_ITEM);
            }
        } else {
            this.createBlockEntity();
        }
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (!target.isTransparent() && face.getIndex() > 1 && !block.isSolid()) {
            switch (face) {
                case NORTH:
                    this.setDamage(3);
                    break;
                case SOUTH:
                    this.setDamage(2);
                    break;
                case WEST:
                    this.setDamage(1);
                    break;
                case EAST:
                    this.setDamage(0);
                    break;
                default:
                    return false;
            }
            this.getLevel().setBlock(block, this, true);
            CompoundTag nbt = BlockEntity.getDefaultCompound(this, BlockEntity.ITEM_FRAME)
                    .putByte("ItemRotation", 0)
                    .putFloat("ItemDropChance", 1);
            if (item.hasCustomBlockData()) {
                for (Tag aTag : item.getCustomBlockData().getAllTags()) {
                    nbt.put(aTag.getName(), aTag);
                }
            }
            new BlockEntityItemFrame(this.getLevel().getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4), nbt);
            this.getLevel().addSound(this, Sound.BLOCK_ITEMFRAME_PLACE);
            return true;
        }
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockAir(), true);
        this.getLevel().addSound(this, Sound.BLOCK_ITEMFRAME_REMOVE_ITEM);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (blockEntity instanceof BlockEntityItemFrame) {
            BlockEntityItemFrame itemFrame = (BlockEntityItemFrame) blockEntity;
            Item itemOnFrame = itemFrame.getItem();
            if (itemOnFrame != null && !itemOnFrame.isNull() && ThreadLocalRandom.current().nextInt(1, 101) <= (itemFrame.getItemDropChance() * 100)) {
                return new Item[]{
                        this.toItem(),
                        itemOnFrame
                };
            }
        }
        return new Item[]{
                this.toItem()
        };
    }

    @Override
    public Item toItem() {
        return Item.get(Item.ITEM_FRAME);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);

        if (blockEntity instanceof BlockEntityItemFrame) {
            return ((BlockEntityItemFrame) blockEntity).getAnalogOutput();
        }

        return super.getComparatorInputOverride();
    }

    private BlockEntityItemFrame createBlockEntity() {
        return new BlockEntityItemFrame(this.getLevel().getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4), BlockEntity.getDefaultCompound(this, BlockEntity.ITEM_FRAME)
                .putByte("ItemRotation", 0)
                .putFloat("ItemDropChance", 1));
    }

    public BlockFace getFacing() {
        switch (this.getDamage() % 8) {
            case 0:
                return BlockFace.WEST;
            case 1:
                return BlockFace.EAST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.SOUTH;
        }

        return null;
    }
}
