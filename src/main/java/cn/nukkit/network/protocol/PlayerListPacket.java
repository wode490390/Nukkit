package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.network.protocol.types.PlayerListEntry;

import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
public class PlayerListPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_LIST_PACKET;

    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;

    public PlayerListEntry[] entries = new PlayerListEntry[0];
    public byte type;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        this.putUnsignedVarInt(this.entries.length);
        for (PlayerListEntry entry : this.entries) {
            this.putUUID(entry.uuid);
            if (this.type == TYPE_ADD) {
                this.putEntityUniqueId(entry.entityUniqueId);
                this.putString(entry.username);
                this.putSkin(entry.skin);
                this.putString(entry.xboxUserId);
                this.putString(entry.platformChatId);
            }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
