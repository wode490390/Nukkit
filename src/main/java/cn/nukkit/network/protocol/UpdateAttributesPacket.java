package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nukkit Project Team
 */
public class UpdateAttributesPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_ATTRIBUTES_PACKET;

    public long entityRuntimeId;
    public List<Attribute> entries = new ArrayList<Attribute>();

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public void decode() {

    }

    public void encode() {
        this.reset();

        this.putEntityRuntimeId(this.entityRuntimeId);

        if (this.entries == null) {
            this.putUnsignedVarInt(0);
        } else {
            this.putUnsignedVarInt(this.entries.size());
            for (Attribute entry : this.entries) {
                this.putLFloat(entry.getMinValue());
                this.putLFloat(entry.getMaxValue());
                this.putLFloat(entry.getValue());
                this.putLFloat(entry.getDefaultValue());
                this.putString(entry.getName());
            }
        }
    }
}
