package cn.nukkit.item;

import cn.nukkit.block.Block;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemBlock extends Item {

    public ItemBlock(int block) {
        this(block, 0);
    }

    public ItemBlock(Block block) {
        this(block, 0);
    }

    public ItemBlock(int block, Integer meta) {
        this(block, meta, 1);
    }

    public ItemBlock(Block block, Integer meta) {
        this(block, meta, 1);
    }

    public ItemBlock(int block, Integer meta, int count) {
        this(Block.get(block, meta), meta, count);
    }

    public ItemBlock(Block block, Integer meta, int count) {
        super(block.getId() > 0xff ? (0xff - block.getId()) & 0xffff : block.getId(), meta, count, block.getName());
        this.block = block;
    }

    public void setDamage(Integer meta) {
        if (meta != null) {
            this.meta = meta & 0xffff;
        } else {
            this.hasMeta = false;
        }
        this.block.setDamage(meta);
    }

    @Override
    public ItemBlock clone() {
        ItemBlock block = (ItemBlock) super.clone();
        block.block = this.block.clone();
        return block;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public int getMaxStackSize() {
        int id = this.getBlock().getId();
        if (id == Block.SHULKER_BOX || id == Block.UNDYED_SHULKER_BOX || id == Block.GLOW_STICK) {
            return 1;
        }

        return super.getMaxStackSize();
    }

    @Override
    public int getMaxDurability() {
        if (this.getBlock().getId() == Block.GLOW_STICK) {
            return 100;
        }

        return super.getMaxDurability();
    }
}
