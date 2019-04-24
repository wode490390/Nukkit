package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemChestplateDiamond extends ItemArmor {

    public ItemChestplateDiamond() {
        this(0);
    }

    public ItemChestplateDiamond(Integer meta) {
        this(meta, 1);
    }

    public ItemChestplateDiamond(Integer meta, int count) {
        super(DIAMOND_CHESTPLATE, meta, 1, "Diamond Chestplate");
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_DIAMOND;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 8;
    }

    @Override
    public int getMaxDurability() {
        return 528;
    }

    @Override
    public int getToughness() {
        return 2;
    }
}
