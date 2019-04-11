package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemBootsDiamond extends ItemArmor {

    public ItemBootsDiamond() {
        this(0);
    }

    public ItemBootsDiamond(Integer meta) {
        this(meta, 1);
    }

    public ItemBootsDiamond(Integer meta, int count) {
        super(DIAMOND_BOOTS, meta, 1, "Diamond Boots");
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_DIAMOND;
    }

    @Override
    public boolean isBoots() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 429;
    }

    @Override
    public int getToughness() {
        return 2;
    }
}
