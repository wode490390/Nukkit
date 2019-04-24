package cn.nukkit.item;

public class ItemHorseArmorLeather extends ItemHorseArmor {

    public ItemHorseArmorLeather() {
        this(0);
    }

    public ItemHorseArmorLeather(Integer meta) {
        this(meta, 1);
    }

    public ItemHorseArmorLeather(Integer meta, int count) {
        super(LEATHER_HORSE_ARMOR, meta, 1, "Leather Horse Armor");
    }
}
