package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockDoorBirch extends BlockDoorWood {

    public BlockDoorBirch() {
        this(0);
    }

    public BlockDoorBirch(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Birch Door Block";
    }

    @Override
    public int getId() {
        return BIRCH_DOOR_BLOCK;
    }

    @Override
    public Item toItem() {
        return Item.get(Item.BIRCH_DOOR);
    }
}
