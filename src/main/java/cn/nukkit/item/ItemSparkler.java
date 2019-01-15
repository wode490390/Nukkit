package cn.nukkit.item;

public class ItemSparkler extends Item {

    public ItemSparkler() {
        this(0, 1);
    }

    public ItemSparkler(Integer meta) {
        this(meta, 1);
    }

    public ItemSparkler(Integer meta, int count) {
        super(SPARKLER, meta, count, "Sparkler");
    }

    @Override
    public boolean isEducation() {
        return true;
    }
}
