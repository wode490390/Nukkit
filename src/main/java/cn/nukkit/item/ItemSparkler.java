package cn.nukkit.item;

public class ItemSparkler extends ItemTool {

    public ItemSparkler() {
        this(0);
    }

    public ItemSparkler(Integer meta) {
        this(meta, 1);
    }

    public ItemSparkler(Integer meta, int count) {
        super(SPARKLER, meta, 1, "Sparkler");
    }

    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_SPARKLER;
    }

    @Override
    public boolean isEducation() {
        return true;
    }
}
