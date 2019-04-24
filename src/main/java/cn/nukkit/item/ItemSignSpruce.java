package cn.nukkit.item;

import cn.nukkit.block.BlockSignPost;//BlockSignPostSpruce

public class ItemSignSpruce extends Item {

    public ItemSignSpruce() {
        this(0);
    }

    public ItemSignSpruce(Integer meta) {
        this(0, 1);
    }

    public ItemSignSpruce(Integer meta, int count) {
        this(0, count, "Spruce Sign");
    }

    public ItemSignSpruce(Integer meta, int count, String name) {
        super(SPRUCE_SIGN, 0, count, name);
        this.block = new BlockSignPost();
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
