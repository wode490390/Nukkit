package cn.nukkit.network.protocol;

public class UpdateTradePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_TRADE_PACKET;

    public byte windowId;
    public byte windowType = 15; //trading id
    public int varint1; //unknown
    public int varint2; //unknown
    public boolean isWilling;
    public long traderEid;
    public long playerEid;
    public String displayName;
    public byte[] offers;

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
        this.putByte(windowId);
        this.putByte(windowType);
        this.putVarInt(varint1);
        this.putVarInt(varint2);
        this.putBoolean(isWilling);
        this.putEntityUniqueId(traderEid);
        this.putEntityUniqueId(playerEid);
        this.putString(displayName);
        this.put(this.offers);
    }

}
