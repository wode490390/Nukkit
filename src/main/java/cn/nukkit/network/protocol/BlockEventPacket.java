package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockEventPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BLOCK_EVENT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int x;
    public int y;
    public int z;
    public int eventType;
    public int eventData;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockPosition(this.x, this.y, this.z);
        this.putVarInt(this.eventType);
        this.putVarInt(this.eventData);
    }
}
