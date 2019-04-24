package cn.nukkit.item;

public abstract class ItemHorseArmor extends Item {

    public ItemHorseArmor(int id) {
        super(id);
    }

    public ItemHorseArmor(int id, Integer meta) {
        super(id, meta);
    }

    public ItemHorseArmor(int id, Integer meta, int count) {
        super(id, meta);
    }

    public ItemHorseArmor(int id, Integer meta, int count, String name) {
        super(id, meta, 1, name);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
