package cn.nukkit.network.protocol;

public class NPCRequestPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.NPC_REQUEST_PACKET;

    public long entityRuntimeId;
    public int requestType;
    public String commandString;
    public int actionType;

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
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putByte((byte) this.requestType);
        this.putString(this.commandString);
        this.putByte((byte) this.actionType);
    }
}
