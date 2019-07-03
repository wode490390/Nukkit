package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.CommandOriginData;
import cn.nukkit.network.protocol.types.CommandOutputMessage;
import lombok.ToString;

@ToString
public class CommandOutputPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.COMMAND_OUTPUT_PACKET;

    public CommandOriginData originData;
    public int outputType;
    public int successCount;
    public CommandOutputMessage[] messages;
    public String unknownString;

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
        this.putCommandOriginData(this.originData);
        this.putByte((byte) this.outputType);
        this.putUnsignedVarInt(this.successCount);

        this.putUnsignedVarInt(this.messages.length);
        for (CommandOutputMessage message : messages) {
            this.putCommandMessage(message);
        }

        if (this.outputType == 4) {
            this.putString(this.unknownString);
        }
    }

    public void putCommandMessage(CommandOutputMessage message) {
        this.putBoolean(message.isInternal);
        this.putString(message.messageId);

        this.putUnsignedVarInt(message.parameters.length);
        for (String parameter : message.parameters) {
            this.putString(parameter);
        }
    }
}
