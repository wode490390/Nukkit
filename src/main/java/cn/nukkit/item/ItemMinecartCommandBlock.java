package cn.nukkit.item;

public class ItemMinecartCommandBlock extends Item {

    public ItemMinecartCommandBlock() {
        this(0, 1);
    }

    public ItemMinecartCommandBlock(Integer meta) {
        this(meta, 1);
    }

    public ItemMinecartCommandBlock(Integer meta, int count) {
        super(COMMAND_BLOCK_MINECART, meta, count, "Minecart with Command Block");
    }
}
