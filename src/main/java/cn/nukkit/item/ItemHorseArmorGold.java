package cn.nukkit.item;

public class ItemHorseArmorGold extends ItemHorseArmor {

    public ItemHorseArmorGold() {
        this(0);
    }

    public ItemHorseArmorGold(Integer meta) {
        this(meta, 1);
    }

    public ItemHorseArmorGold(Integer meta, int count) {
        super(GOLD_HORSE_ARMOR, meta, 1, "Gold Horse Armor");
    }
}
