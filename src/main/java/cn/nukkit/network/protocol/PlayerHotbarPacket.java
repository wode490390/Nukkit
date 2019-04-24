package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.ContainerIds;
import lombok.ToString;

@ToString
public class PlayerHotbarPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_HOTBAR_PACKET;

    public int selectedHotbarSlot;
    public int windowId = ContainerIds.INVENTORY;
    public boolean selectHotbarSlot = true;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.selectedHotbarSlot = (int) this.getUnsignedVarInt();
        this.windowId = this.getByte();
        this.selectHotbarSlot = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.selectedHotbarSlot);
        this.putByte((byte) this.windowId);
        this.putBoolean(this.selectHotbarSlot);
    }
}
