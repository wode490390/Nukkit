package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MobArmorEquipmentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityRuntimeId;
    public Item[] slots = new Item[4];

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.slots = new Item[4];
        for (int i = 0; i < 4; ++i) {
            this.slots[i] = this.getSlot();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.entityRuntimeId);
        for (int i = 0; i < 4; ++i) {
            this.putSlot(this.slots[i]);
        }
    }
}
