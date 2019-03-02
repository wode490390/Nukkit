package cn.nukkit.item;

import cn.nukkit.block.BlockSignPost;//BlockSignPostBirch

public class ItemSignBirch extends Item {

    public ItemSignBirch() {
        this(0);
    }

    public ItemSignBirch(Integer meta) {
        this(0, 1);
    }

    public ItemSignBirch(Integer meta, int count) {
        this(0, count, "Birch Sign");
    }

    public ItemSignBirch(Integer meta, int count, String name) {
        super(BIRCH_SIGN, 0, count, name);
        this.block = new BlockSignPost();
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
