package cn.nukkit.network.protocol;

public class InitiateWebSocketConnectionPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INITIATE_WEB_SOCKET_CONNECTION_PACKET;

    public String serverUri;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.serverUri = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.serverUri);
    }
}
