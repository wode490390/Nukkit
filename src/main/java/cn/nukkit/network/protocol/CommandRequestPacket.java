package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.CommandOriginData;
import lombok.ToString;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@ToString
public class CommandRequestPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.COMMAND_REQUEST_PACKET;

    public String command;
    public CommandOriginData originData;
    public boolean isInternal;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.command = this.getString();
        this.originData = this.getCommandOriginData();
        this.isInternal = this.getBoolean();
    }

    @Override
    public void encode() {

    }
}
