package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class RemoveObjectivePacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.REMOVE_OBJECTIVE_PACKET;

    public String objectiveName;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.objectiveName = getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.objectiveName);
    }
}
