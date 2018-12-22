package cn.nukkit.item;

public class ItemKelp extends Item {

    public ItemKelp() {
        this(0, 1);
    }

    public ItemKelp(Integer meta) {
        this(meta, 1);
    }

    public ItemKelp(Integer meta, int count) {
        super(KELP, meta, count, "Kelp");
    }
}
