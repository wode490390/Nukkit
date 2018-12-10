package cn.nukkit.network.protocol;

public class NetworkStackLatencyPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.NETWORK_STACK_LATENCY_PACKET;

    public long timestamp;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.timestamp = this.getLLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putLLong(this.timestamp);
    }
}
