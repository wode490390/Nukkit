package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created on 2015/12/27 by xtypr.
 * Package cn.nukkit.item in project Nukkit .
 */
public class ItemPotionSplash extends ProjectileItem {

    public ItemPotionSplash() {
        this(0, 1);
    }

    public ItemPotionSplash(Integer meta) {
        this(meta, 1);
    }

    public ItemPotionSplash(Integer meta, int count) {
        super(SPLASH_POTION, meta, count, "Splash Potion");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public String getProjectileEntityType() {
        return "ThrownPotion";
    }

    @Override
    public float getThrowForce() {
        return 0.5f;
    }

    @Override
    protected void correctNBT(CompoundTag nbt) {
        /*TODO: CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<CompoundTag>("CustomPotionEffects")
                        .add(new CompoundTag()
                                .putByte("Id", id)
                                //.putByte("Amplifier", amplifier)
                                //.putInt("Duration", time)
                .putString("Potion", name)
                .putInt("CustomPotionColor", color);//(R << 16) + (G << 8) + B*/
        nbt.putInt("PotionId", this.meta);
    }
}
