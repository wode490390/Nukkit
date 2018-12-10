package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.utils.Binary;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId = 0; //TODO
    public long entityRuntimeId = 0;
    public int type = 0;
    public Vector3f position = new Vector3f();
    public Vector3f motion = new Vector3f();
    public float pitch = 0f;
    public float yaw = 0f;
    public float headYaw = 0f;

    public Attribute[] attributes = new Attribute[0];
    public EntityMetadata metadata = new EntityMetadata();
    public EntityLink[] links = new EntityLink[0];

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putUnsignedVarInt(this.type);
        this.putVector3(this.position);
        this.putVector3Nullable(this.motion);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putLFloat(this.headYaw);

        this.putUnsignedVarInt(this.attributes.length);
        for (Attribute attribute : attributes) {
            this.putString(attribute.getId());
            this.putLFloat(attribute.getMinValue());
            this.putLFloat(attribute.getValue());
            this.putLFloat(attribute.getMaxValue());
        }

        this.put(Binary.writeMetadata(this.metadata));
        this.putUnsignedVarInt(this.links.length);
        for (EntityLink link : this.links) {
            this.putEntityLink(link);
        }
    }
}
