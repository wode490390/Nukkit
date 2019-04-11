package cn.nukkit.item;

public class ItemSuperFertilizer extends Item {

    public ItemSuperFertilizer() {
        this(0, 1);
    }

    public ItemSuperFertilizer(Integer meta) {
        this(meta, 1);
    }

    public ItemSuperFertilizer(Integer meta, int count) {
        super(SUPER_FERTILIZER, meta, count, "Super Fertilizer");
    }

    @Override
    public boolean isEducation() {
        return true;
    }
}
