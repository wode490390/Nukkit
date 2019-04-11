package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemBookEnchanted extends Item {

    public ItemBookEnchanted() {
        this(0);
    }

    public ItemBookEnchanted(Integer meta) {
        this(meta, 1);
    }

    public ItemBookEnchanted(Integer meta, int count) {
        super(ENCHANTED_BOOK, meta, 1, "Enchanted Book");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
