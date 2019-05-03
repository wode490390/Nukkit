package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class VideoStreamConnectPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.VIDEO_STREAM_CONNECT_PACKET;

    public static final byte ACTION_CONNECT = 0;
    public static final byte ACTION_DISCONNECT = 1;

    public String serverUri;
    public float frameSendFrequency;
    public byte action;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putString(this.serverUri);
        this.putLFloat(this.frameSendFrequency);
        this.putByte(this.action);
    }
}
