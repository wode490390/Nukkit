package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.Tag;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class ItemTool extends Item {

    public static final int TIER_WOODEN = 1;
    public static final int TIER_GOLD = 2;
    public static final int TIER_STONE = 3;
    public static final int TIER_IRON = 4;
    public static final int TIER_DIAMOND = 5;

    public static final int TYPE_NONE = 0;
    public static final int TYPE_SWORD = 1;
    public static final int TYPE_SHOVEL = 2;
    public static final int TYPE_PICKAXE = 3;
    public static final int TYPE_AXE = 4;
    public static final int TYPE_SHEARS = 5;

    public static final int DURABILITY_WOODEN = 59;
    public static final int DURABILITY_GOLD = 32;
    public static final int DURABILITY_STONE = 131;
    public static final int DURABILITY_IRON = 250;
    public static final int DURABILITY_DIAMOND = 1561;

    public static final int DURABILITY_FLINT_STEEL = 64;
    public static final int DURABILITY_SHEARS = 238;
    public static final int DURABILITY_BOW = 384;
    public static final int DURABILITY_TRIDENT = 250;
    public static final int DURABILITY_CROSSBOW = 464;
    public static final int DURABILITY_CARROT_ON_A_STICK = 26;
    public static final int DURABILITY_FISHING_ROD = 384;
    public static final int DURABILITY_SPARKLER = 100;

    public ItemTool(int id) {
        super(id);
    }

    public ItemTool(int id, Integer meta) {
        super(id, meta);
    }

    public ItemTool(int id, Integer meta, int count) {
        super(id, meta);
    }

    public ItemTool(int id, Integer meta, int count, String name) {
        super(id, meta, 1, name);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean useOn(Block block) {
        if (this.isUnbreakable() || isDurable()) {
            return true;
        }

        if (block.getToolType() == ItemTool.TYPE_PICKAXE && this.isPickaxe() ||
                block.getToolType() == ItemTool.TYPE_SHOVEL && this.isShovel() ||
                block.getToolType() == ItemTool.TYPE_AXE && this.isAxe() ||
                block.getToolType() == ItemTool.TYPE_SWORD && this.isSword() ||
                block.getToolType() == ItemTool.SHEARS && this.isShears()
                ) {
            this.meta++;
        } else if (!this.isShears() && block.getBreakTime(this) > 0) {
            this.meta += 2;
        } else if (this.isHoe()) {
            if (block.getId() == GRASS || block.getId() == DIRT) {
                this.meta++;
            }
        } else {
            this.meta++;
        }
        return true;
    }

    @Override
    public boolean useOn(Entity entity) {
        if (this.isUnbreakable() || isDurable()) {
            return true;
        }

        if ((entity != null) && !this.isSword()) {
            this.meta += 2;
        } else {
            this.meta++;
        }

        return true;
    }

    private boolean isDurable() {
        if (!hasEnchantments()) {
            return false;
        }

        Enchantment durability = getEnchantment(Enchantment.ID_DURABILITY);
        return durability != null && durability.getLevel() > 0 && (100 / (durability.getLevel() + 1)) <= ThreadLocalRandom.current().nextInt(100);
    }

    @Override
    public boolean isUnbreakable() {
        Tag tag = this.getNamedTagEntry("Unbreakable");
        return tag instanceof ByteTag && ((ByteTag) tag).data > 0;
    }

    @Override
    public boolean isPickaxe() {
        return false;
    }

    @Override
    public boolean isAxe() {
        return false;
    }

    @Override
    public boolean isSword() {
        return false;
    }

    @Override
    public boolean isShovel() {
        return false;
    }

    @Override
    public boolean isHoe() {
        return false;
    }

    @Override
    public boolean isShears() {
        return (this.id == SHEARS);
    }

    @Override
    public boolean isTool() {
        return (this.id == FISHING_ROD || this.id == CARROT_ON_A_STICK || this.id == FLINT_STEEL || this.id == SHEARS || this.id == BOW || this.id == CROSSBOW || this.id == SPARKLER || this.isPickaxe() || this.isAxe() || this.isShovel() || this.isSword() || this.isHoe());
    }

    @Override
    public int getEnchantAbility() {
        switch (this.getTier()) {
            case TIER_STONE:
                return 5;
            case TIER_WOODEN:
                return 15;
            case TIER_DIAMOND:
                return 10;
            case TIER_GOLD:
                return 22;
            case TIER_IRON:
                return 14;
        }

        return 0;
    }
}
