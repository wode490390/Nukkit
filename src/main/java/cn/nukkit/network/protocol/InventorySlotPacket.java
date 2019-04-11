package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class InventorySlotPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INVENTORY_SLOT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int windowId;
    public int inventorySlot;
    public Item item;

    @Override
    public void decode() {
        this.windowId = (int) this.getUnsignedVarInt();
        this.inventorySlot = (int) this.getUnsignedVarInt();
        this.item = this.getSlot();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt((byte) this.windowId);
        this.putUnsignedVarInt(this.inventorySlot);
        this.putSlot(this.item);
    }
}
