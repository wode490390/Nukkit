package cn.nukkit.item;

public class ItemShield extends ItemTool {

    public ItemShield() {
        this(0);
    }

    public ItemShield(Integer meta) {
        this(meta, 1);
    }

    public ItemShield(Integer meta, int count) {
        super(SHIELD, meta, 1, "Shield");
    }

    @Override
    public int getMaxDurability() {
        return DURABILITY_SHIELD;
    }
}
