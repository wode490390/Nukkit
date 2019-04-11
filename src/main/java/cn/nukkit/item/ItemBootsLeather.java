package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemBootsLeather extends ItemColorArmor {

    public ItemBootsLeather() {
        this(0);
    }

    public ItemBootsLeather(Integer meta) {
        this(meta, 1);
    }

    public ItemBootsLeather(Integer meta, int count) {
        super(LEATHER_BOOTS, meta, 1, "Leather Boots");
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_LEATHER;
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 65;
    }
}
