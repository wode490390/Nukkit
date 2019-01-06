package cn.nukkit.item.enchantment.curse;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class EnchantmentCurse extends Enchantment {

    protected EnchantmentCurse(int id, String name, EnchantmentType type) {
        super(id, name, 1, type);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 25;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "%enchantment.curse." + this.name;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
