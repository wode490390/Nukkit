package cn.nukkit.item;

public class ItemEyeDrop extends Item {

    public ItemEyeDrop() {
        this(0);
    }

    public ItemEyeDrop(Integer meta) {
        this(meta, 1);
    }

    public ItemEyeDrop(Integer meta, int count) {
        super(EYE_DROP, meta, 1, "Potion");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isEducation() {
        return true;
    }
}
