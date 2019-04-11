package cn.nukkit.item;

import cn.nukkit.block.BlockSignPost;//BlockSignPostDarkOak

public class ItemSignDarkOak extends Item {

    public ItemSignDarkOak() {
        this(0);
    }

    public ItemSignDarkOak(Integer meta) {
        this(0, 1);
    }

    public ItemSignDarkOak(Integer meta, int count) {
        this(0, count, "Dark Oak Sign");
    }

    public ItemSignDarkOak(Integer meta, int count, String name) {
        super(DARK_OAK_SIGN, 0, count, name);
        this.block = new BlockSignPost();
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
