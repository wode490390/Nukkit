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

    protected Item hand = Item.get(Item.AIR);
    protected Item offhand = Item.get(Item.AIR);

    protected Item helmet = Item.get(Item.AIR);
    protected Item chestplate = Item.get(Item.AIR);
    protected Item leggings = Item.get(Item.AIR);
    protected Item boots = Item.get(Item.AIR);

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

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.hasBaby()) {
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, false);
        }
    }

    public void setBaby(boolean baby) {
        if (this.hasBaby()) {
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, baby);
            this.setScale(baby ? 0.5f : 1);
            this.setMovementSpeed(baby ? 0.12f : 0.1f);
        }
    }

    public boolean isBaby() {
        return this.hasBaby() && this.getDataFlag(DATA_FLAGS, DATA_FLAG_BABY);
    }

    public boolean hasBaby() {
        return false;
    }

    public int getXpDropAmount() {
        return 0;
    }

    public Item getItemInHand() {
        return this.hand;
    }

    public void setItemInHand(Item item) {
        this.hand = item;
    }

    public Item getItemInOffhand() {
        return this.offhand;
    }

    public void setItemInOffhand(Item item) {
        this.offhand = item;
    }

    public Item getHelmet() {
        return this.helmet;
    }

    public void setHelmet(Item item) {
        if (item.isHelmet()) {
            this.helmet = item;
        } else {
            this.helmet = Item.get(Item.AIR);
        }
    }

    public Item getChestplate() {
        return this.chestplate;
    }

    public void setChestplate(Item item) {
        if (item.isChestplate()) {
            this.chestplate = item;
        } else {
            this.chestplate = Item.get(Item.AIR);
        }
    }

    public Item getLeggings() {
        return this.leggings;
    }

    public void setLeggings(Item item) {
        if (item.isLeggings()) {
            this.leggings = item;
        } else {
            this.leggings = Item.get(Item.AIR);
        }
    }

    public Item getBoots() {
        return this.boots;
    }

    public void setBoots(Item item) {
        if (item.isBoots()) {
            this.boots = item;
        } else {
            this.boots = Item.get(Item.AIR);
        }
    }

    public Item[] getArmorContents() {
        return new Item[]{
                this.getHelmet(),
                this.getChestplate(),
                this.getLeggings(),
                this.getBoots()
        };
    }

    public void clearAllArmors() {
        this.setHelmet(Item.get(Item.AIR));
        this.setChestplate(Item.get(Item.AIR));
        this.setLeggings(Item.get(Item.AIR));
        this.setBoots(Item.get(Item.AIR));
    }
}
