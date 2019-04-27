package cn.nukkit.item;

public class ItemCampfire extends Item {

    public ItemCampfire() {
        this(0);
    }

    public ItemCampfire(Integer meta) {
        this(meta, 1);
    }

    public ItemCampfire(Integer meta, int count) {
        super(CAMPFIRE, meta, count, "Campfire");
        //this.block = new BlockCampfire();
    }
}
