package cn.nukkit.item;

/**
 * Created by PetteriM1
 */
public class ItemTurtleShell extends ItemArmor {

    public ItemTurtleShell() {
        this(0);
    }

    public ItemTurtleShell(Integer meta) {
        this(meta, 1);
    }

    public ItemTurtleShell(Integer meta, int count) {
        super(TURTLE_SHELL, meta, 1, "Turtle Shell");
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_OTHER;
    }

    @Override
    public boolean isHelmet() {
        return true;
    }

    @Override
    public int getArmorPoints() {
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 275;
    }

    @Override
    public int getToughness() {
        return 2;
    }
}
