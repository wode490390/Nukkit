package cn.nukkit.item;

public class ItemIceBomb extends Item {

    public ItemIceBomb() {
        this(0, 1);
    }

    public ItemIceBomb(Integer meta) {
        this(meta, 1);
    }

    public ItemIceBomb(Integer meta, int count) {
        super(ICE_BOMB, meta, count, "Ice Bomb");
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public boolean isEducation() {
        return true;
    }
}
