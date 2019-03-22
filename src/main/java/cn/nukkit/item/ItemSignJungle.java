package cn.nukkit.item;

import cn.nukkit.block.BlockSignPost;//BlockSignPostJungle

public class ItemSignJungle extends Item {

    public ItemSignJungle() {
        this(0);
    }

    public ItemSignJungle(Integer meta) {
        this(0, 1);
    }

    public ItemSignJungle(Integer meta, int count) {
        this(0, count, "Jungle Sign");
    }

    public ItemSignJungle(Integer meta, int count, String name) {
        super(JUNGLE_SIGN, 0, count, name);
        this.block = new BlockSignPost();
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
