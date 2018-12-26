package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class EntityAnimal extends EntityCreature {

    public EntityAnimal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.hasBaby()) {
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, false)
        }
    }

    public boolean isBaby() {
        return this.hasBaby() && this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    public void setBaby(boolean baby) {
        if (this.hasBaby()) {
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, baby);
            this.setScale(baby ? 0.5f : 1);
        }
    }

    public boolean hasBaby() {
        return false;
    }

    public boolean isBreedingItem(Item item) {
        return item.getId() == Item.WHEAT; //default
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (item.getId() == Item.NAME_TAG) {
            if (item.hasCustomName()) {
                this.setNameTag(item.getCustomName());
                this.setNameTagVisible(true);
                player.getInventory().removeItem(item);
                return true;
            }
        }
        return false;
    }

    public int getXpDropAmount() {
        return 0;
    }
}
