package cn.nukkit.network.protocol;

public class StructureBlockUpdatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        //TODO
    }
}
