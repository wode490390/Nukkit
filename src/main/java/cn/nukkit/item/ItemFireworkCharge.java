package cn.nukkit.item;

public class ItemFireworkCharge extends Item {

    public ItemFireworkCharge() {
        this(0, 1);
    }

    public ItemFireworkCharge(Integer meta) {
        this(meta, 1);
    }

    public ItemFireworkCharge(Integer meta, int count) {
        super(FIREWORKSCHARGE, meta, count, "Firework Star");
    }
}
