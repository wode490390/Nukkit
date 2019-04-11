package cn.nukkit.item;

public class ItemIronNugget extends Item {

    public ItemIronNugget() {
        this(0, 1);
    }

    public ItemIronNugget(Integer meta) {
        this(meta, 1);
    }

    public ItemIronNugget(Integer meta, int count) {
        super(IRON_NUGGET, meta, count, "Iron Nugget");
    }
}
