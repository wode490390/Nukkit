package cn.nukkit.item;

public class ItemGlowStick extends ItemTool {

    public ItemGlowStick() {
        this(0);
    }

    public ItemGlowStick(Integer meta) {
        this(meta, 1);
    }

    public ItemGlowStick(Integer meta, int count) {
        super(GLOW_STICK, meta, 1, "Glow Stick");
    }

    @Override
    public int getMaxDurability() {
        return DURABILITY_GLOW_STICK;
    }
}
