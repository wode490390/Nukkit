package cn.nukkit.item;

public class ItemHorseArmorIron extends ItemHorseArmor {

    public ItemHorseArmorIron() {
        this(0);
    }

    public ItemHorseArmorIron(Integer meta) {
        this(meta, 1);
    }

    public ItemHorseArmorIron(Integer meta, int count) {
        super(IRON_HORSE_ARMOR, meta, 1, "Iron Horse Armor");
    }
}
