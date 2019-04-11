package cn.nukkit.network.protocol;

public class AutomationClientConnectPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.AUTOMATION_CLIENT_CONNECT_PACKET;

    public String serverUri;

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
        this.putString(this.serverUri);
    }
}
