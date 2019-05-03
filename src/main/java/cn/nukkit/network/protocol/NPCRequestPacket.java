package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class NPCRequestPacket extends DataPacket implements ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.NPC_REQUEST_PACKET;

    public static final int TYPE_SET_ACTION = 0;
    public static final int TYPE_EXECUTE_COMMAND_ACTION = 1;
    public static final int TYPE_EXECUTE_CLOSING_COMMANDS = 2;

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
