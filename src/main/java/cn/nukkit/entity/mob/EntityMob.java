package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class EntityMob extends EntityCreature {

    public EntityMob(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
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

    public void setBaby(boolean baby) {
        if (this.hasBaby()) {
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, baby);
            this.setScale(baby ? 0.5f : 1);
            this.setMovementSpeed(baby ? 0.12f : 0.1f);
        }
    }

    public boolean isBaby() {
        return this.hasBaby() && this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    public boolean hasBaby() {
        return false;
    }

    public int getXpDropAmount() {
        return 0;
    }
}
