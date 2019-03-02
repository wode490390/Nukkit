package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemChestplateLeather extends ItemColorArmor {

    public ItemChestplateLeather() {
        this(0);
    }

    public ItemChestplateLeather(Integer meta) {
        this(meta, 1);
    }

    public ItemChestplateLeather(Integer meta, int count) {
        super(LEATHER_TUNIC, meta, 1, "Leather Tunic");
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_LEATHER;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 80;
    }
}
