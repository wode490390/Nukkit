package cn.nukkit.network.protocol;

public class SubClientLoginPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SUB_CLIENT_LOGIN_PACKET;

    public String connectionRequestData;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.connectionRequestData);
    }
}
