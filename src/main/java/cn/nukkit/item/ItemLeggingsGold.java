package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemLeggingsGold extends ItemArmor {

    public ItemLeggingsGold() {
        this(0);
    }

    public ItemLeggingsGold(Integer meta) {
        this(meta, 1);
    }

    public ItemLeggingsGold(Integer meta, int count) {
        super(GOLD_LEGGINGS, meta, 1, "Gold Leggings");
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_GOLD;
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 3;
    }

    @Override
    public int getMaxDurability() {
        return 105;
    }
}
