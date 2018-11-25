package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.ScoreboardIdentityPacketEntry;

import java.util.List;

public class SetScoreboardIdentityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_SCOREBOARD_IDENTITY_PACKET;

    public static final int TYPE_REGISTER_IDENTITY = 0;
    public static final int TYPE_CLEAR_IDENTITY = 1;

    public int type;
    public List<ScoreboardIdentityPacketEntry> entries;

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
        this.putByte((byte) this.type);
        this.putUnsignedVarInt(this.entries.size());
        for (ScoreboardIdentityPacketEntry entry : entries) {
            this.putVarLong(entry.scoreboardId);
            if (this.type == TYPE_REGISTER_IDENTITY) {
                this.putEntityUniqueId(entry.entityUniqueId);
            }
        }
    }
}
