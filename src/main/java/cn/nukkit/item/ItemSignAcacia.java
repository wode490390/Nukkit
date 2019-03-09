package cn.nukkit.item;

import cn.nukkit.block.BlockSignPost;//BlockSignPostAcacia

public class ItemSignAcacia extends Item {

    public ItemSignAcacia() {
        this(0);
    }

    public ItemSignAcacia(Integer meta) {
        this(0, 1);
    }

    public ItemSignAcacia(Integer meta, int count) {
        this(0, count, "Acacia Sign");
    }

    public ItemSignAcacia(Integer meta, int count, String name) {
        super(ACACIA_SIGN, 0, count, name);
        this.block = new BlockSignPost();
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
