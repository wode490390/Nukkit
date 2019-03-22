package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

public class CommandBlockInventory extends MenuInventory {

    public CommandBlockInventory(Position position) {
        super(InventoryType.COMMAND_BLOCK, position);
    }

    @Override
    public void onOpen(Player who) {
        if (who.isOp() && who.isCreative()) {
            super.onOpen(who);
        }
    }
}
