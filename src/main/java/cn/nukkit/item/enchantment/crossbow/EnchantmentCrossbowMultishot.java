package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;

public class EnchantmentCrossbowMultishot extends EnchantmentCrossbow {

    public EnchantmentCrossbowMultishot() {
        super(Enchantment.ID_CROSSBOW_MULTISHOT, "crossbowMultishot", 1);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 20;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
