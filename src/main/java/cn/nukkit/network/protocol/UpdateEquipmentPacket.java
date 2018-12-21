package cn.nukkit.network.protocol;

public class UpdateEquipmentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_EQUIPMENT_PACKET;

    public int windowId;
    public int windowType;
    public int unknownVarint; //TODO: find out what this is (vanilla always sends 0)
    public long entityUniqueId;
    public byte[] namedtag;

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
        this.putByte((byte) this.windowId);
        this.putByte((byte) this.windowType);
        this.putVarInt(this.unknownVarint);
        this.putEntityUniqueId(this.entityUniqueId);
        this.put(this.namedtag);
    }
}
