package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemHelmetLeather extends ItemColorArmor {

    public ItemHelmetLeather() {
        this(0);
    }

    public ItemHelmetLeather(Integer meta) {
        this(meta, 1);
    }

    public ItemHelmetLeather(Integer meta, int count) {
        super(LEATHER_CAP, meta, 1, "Leather Cap");
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_LEATHER;
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 55;
    }
}
