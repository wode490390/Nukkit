package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemElytra extends ItemArmor {

    public ItemElytra() {
        this(0);
    }

    public ItemElytra(Integer meta) {
        this(meta, 1);
    }

    public ItemElytra(Integer meta, int count) {
        super(ELYTRA, meta, 1, "Elytra");
    }

    @Override
    public int getMaxDurability() {
        return 432;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }
}
