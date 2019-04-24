package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.WindowTypes;
import lombok.ToString;

@ToString
public class UpdateTradePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_TRADE_PACKET;

    public byte windowId;
    public byte windowType = WindowTypes.TRADING; //trading id
    public int varint1; //unknown, hardcoded to 0
    public int tradeTier;
    public long traderEid;
    public long playerEid;
    public String displayName;
    public boolean screen2;
    public boolean isWilling;
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
        this.putByte(this.windowId);
        this.putByte(this.windowType);
        this.putVarInt(this.varint1);
        this.putVarInt(this.tradeTier);
        this.putEntityUniqueId(this.traderEid);
        this.putEntityUniqueId(this.playerEid);
        this.putString(this.displayName);
        this.putBoolean(this.screen2);
        this.putBoolean(this.isWilling);
        this.put(this.offers);
    }
}
