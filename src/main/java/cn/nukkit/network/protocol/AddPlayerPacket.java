package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.utils.Binary;
import java.util.UUID;
import lombok.ToString;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@ToString
public class AddPlayerPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_PLAYER_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public UUID uuid;
    public String username;
    public long entityUniqueId;
    public long entityRuntimeId;
    public String platformChatId = "";
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float pitch;
    public float yaw;
    public Item item;
    public EntityMetadata metadata = new EntityMetadata();
    public EntityLink[] links = new EntityLink[0];
    public String deviceId = "";
    public int playerFlags = 0;
    public int commandPermission = 0;
    public int worldFlags = 0;
    public int playerPermission = 0;
    public int customFlags = 0;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putUUID(this.uuid);
        this.putString(this.username);
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putString(this.platformChatId);
        this.putVector3(this.x, this.y, this.z);
        this.putVector3(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw); //TODO headrot
        this.putLFloat(this.yaw);
        this.putSlot(this.item);
        this.put(Binary.writeMetadata(this.metadata));
        this.putUnsignedVarInt(this.playerFlags);
        this.putUnsignedVarInt(this.commandPermission);
        this.putUnsignedVarInt(this.worldFlags);
        this.putUnsignedVarInt(this.playerPermission);
        this.putUnsignedVarInt(this.customFlags);
        this.putLLong(entityUniqueId);
        this.putUnsignedVarInt(this.links.length);
        for (EntityLink link : this.links) {
            this.putEntityLink(link);
        }
        this.putString(deviceId);
    }
}
