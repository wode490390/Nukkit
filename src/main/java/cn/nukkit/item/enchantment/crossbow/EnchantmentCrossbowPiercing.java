package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;

public class EnchantmentCrossbowPiercing extends EnchantmentCrossbow {

    public EnchantmentCrossbowPiercing() {
        super(Enchantment.ID_CROSSBOW_PIERCING, "crossbowPiercing", 1);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }
}
