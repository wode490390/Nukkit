package cn.nukkit.network.protocol;

/**
 * Created by CreeperFace on 5.3.2017.
 */
public class MapInfoRequestPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MAP_INFO_REQUEST_PACKET;

    public long mapId;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.mapId = this.getEntityUniqueId();
    }

    @Override
    public void encode() {

    }
}
