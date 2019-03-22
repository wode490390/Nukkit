package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.network.protocol.InventorySlotPacket;
import cn.nukkit.network.protocol.types.ContainerIds;

/**
 * @author CreeperFace
 */
public class PlayerOffhandInventory extends BaseInventory {

    public PlayerOffhandInventory(EntityHumanType holder) {
        super(holder, InventoryType.OFFHAND);
    }

    @Override
    public String getName() {
        return "Offhand";
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void setSize(int size) {
        throw new RuntimeException("Offhand can only carry one item at a time");
    }

    @Override
    public void sendSlot(int index, Player... target) {
        InventorySlotPacket pk = new InventorySlotPacket();
        pk.inventorySlot = index;
        pk.item = this.getItem(index);

        for (Player p : target) {
            if (p == this.getHolder()) {
                pk.windowId = ContainerIds.OFFHAND;
                p.dataPacket(pk);
            } else {
                int id;

                if ((id = p.getWindowId(this)) == ContainerIds.NONE) {
                    this.close(p);
                    continue;
                }
                pk.windowId = id;
                p.dataPacket(pk);
            }
        }
    }

    @Override
    public EntityHuman getHolder() {
        return (EntityHuman) this.holder;
    }
}
