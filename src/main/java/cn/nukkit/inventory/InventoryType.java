package cn.nukkit.inventory;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public enum InventoryType {
    
    PLAYER(40, "Player", -1), //36 CONTAINER, 4 ARMOR
    CURSOR(1, "Cursor", -1),
    OFFHAND(1, "Offhand", -1),
    CHEST(27, "Chest", 0),
    ENDER_CHEST(27, "Ender Chest", 0),
    SHULKER_BOX(27, "Shulker Box", 0),
    DOUBLE_CHEST(27 + 27, "Double Chest", 0),
    CRAFTING(5, "Crafting", 1), //4 CRAFTING slots, 1 RESULT
    WORKBENCH(10, "Crafting", 1), //9 CRAFTING slots, 1 RESULT
    FURNACE(3, "Furnace", 2),
    ENCHANT_TABLE(2, "Enchant", 3), //1 INPUT/OUTPUT, 1 LAPIS
    BREWING_STAND(5, "Brewing", 4), //1 INPUT, 3 POTION, 1 fuel
    ANVIL(3, "Anvil", 5), //2 INPUT, 1 OUTPUT
    DISPENSER(0, "Dispenser", 6), //9 CONTAINER
    DROPPER(9, "Dropper", 7), //9 CONTAINER
    HOPPER(5, "Hopper", 8), //5 CONTAINER
    //CAULDRON(1, "Cauldron", 9),
    MINECART_CHEST(27, "Minecart Chest", 10),
    MINECART_HOPPER(5, "Minecart Hopper", 11),
    HORSE(2, "Horse", 12),
    BEACON(1, "Beacon", 13),
    //STRUCTURE_EDITOR(1, "Structure Editor", 14),
    TRADING(3, "Trading", 15);
    //COMMAND_BLOCK(1, "Command Block", 16),
    //JUKEBOX(1, "Jukebox", 17);

    private final int size;
    private final String title;
    private final int typeId;

    InventoryType(int defaultSize, String defaultBlockEntity, int typeId) {
        this.size = defaultSize;
        this.title = defaultBlockEntity;
        this.typeId = typeId;
    }

    public int getDefaultSize() {
        return size;
    }

    public String getDefaultTitle() {
        return title;
    }

    public int getNetworkType() {
        return typeId;
    }
}
