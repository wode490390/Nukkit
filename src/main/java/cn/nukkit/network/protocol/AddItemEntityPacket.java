package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Binary;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddItemEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_ITEM_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId; //TODO
    public long entityRuntimeId;
    public Item item;
    public Vector3 position;
    public Vector3 motion;
    public EntityMetadata metadata = new EntityMetadata();
    public boolean isFromFishing = false;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putSlot(this.item);
        this.putVector3(this.position.asVector3f());
        this.putVector3(this.motion.asVector3f());
        this.put(Binary.writeMetadata(metadata));
        this.putBoolean(this.isFromFishing);
    }
}
