package cn.nukkit.item;

public class ItemHorseArmorDiamond extends ItemHorseArmor {

    public ItemHorseArmorDiamond() {
        this(0);
    }

    public ItemHorseArmorDiamond(Integer meta) {
        this(meta, 1);
    }

    public ItemHorseArmorDiamond(Integer meta, int count) {
        super(DIAMOND_HORSE_ARMOR, meta, 1, "Diamond Horse Armor");
    }
}
