package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerOpenPacket;

public class MenuInventory extends BaseInventory {

    public MenuInventory(InventoryType type, Position position) {
        super(null, type);
        this.holder = new FakeBlockMenu(this, position);
    }

    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = who.getWindowId(this);
        pk.type = this.getType().getNetworkType();
        InventoryHolder holder = this.getHolder();
        if (holder instanceof Vector3) {
            pk.x = ((Vector3) holder).getFloorX();
            pk.y = ((Vector3) holder).getFloorY();
            pk.z = ((Vector3) holder).getFloorZ();
        } else {
            pk.x = pk.y = pk.z = 0;
        }

        who.dataPacket(pk);

        this.sendContents(who);
    }

    @Override
    public FakeBlockMenu getHolder() {
        return (FakeBlockMenu) this.holder;
    }
}
