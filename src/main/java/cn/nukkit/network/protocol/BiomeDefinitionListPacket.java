package cn.nukkit.network.protocol;

public class BiomeDefinitionListPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BIOME_DEFINITION_LIST_PACKET;

    public byte[] namedtag;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.namedtag = this.get();
    }

    @Override
    public void encode() {
        this.reset();
        this.put(this.namedtag);
    }
}
