package cn.nukkit.item;

public class ItemCustomWode extends Item {

    public ItemCustomWode() {
        this(0);
    }

    public ItemCustomWode(Integer meta) {
        this(meta, 1);
    }

    public ItemCustomWode(Integer meta, int count) {
        super(490, meta, count, "Wode Custom Item");
    }
}
