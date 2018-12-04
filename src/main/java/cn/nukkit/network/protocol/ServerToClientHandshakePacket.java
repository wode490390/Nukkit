package cn.nukkit.network.protocol;

public class ServerToClientHandshakePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SERVER_TO_CLIENT_HANDSHAKE_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public String jwt;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putString(this.jwt);
    }

    @Override
    public boolean canBeSentBeforeLogin() {
        return true;
    }
}
