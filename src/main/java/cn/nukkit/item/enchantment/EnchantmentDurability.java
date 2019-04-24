package cn.nukkit.item.enchantment;

import cn.nukkit.item.Item;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EnchantmentDurability extends Enchantment {

    protected EnchantmentDurability() {
        super(ID_DURABILITY, "durability", 5, EnchantmentType.BREAKABLE);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return super.isCompatibleWith(enchantment) && enchantment.id != ID_FORTUNE_DIGGING;
    }

    @Override
    public boolean canEnchant(Item item) {
        return item.getMaxDurability() >= 0 || super.canEnchant(item);
    }

    public static boolean negateDamage(Item item, int level) {
        return !(item.isArmor() && ThreadLocalRandom.current().nextFloat() < 0.6f) && ThreadLocalRandom.current().nextInt(level + 1) > 0;
    }
}
