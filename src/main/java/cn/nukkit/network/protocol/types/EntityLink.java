package cn.nukkit.network.protocol.types;

import lombok.ToString;

@ToString
public class EntityLink {

    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDER = 1;
    public static final byte TYPE_PASSENGER = 2;

    public long fromEntityUniqueId;
    public long toEntityUniqueId;
    public byte type;
    public boolean immediate; //for dismounting on mount death

    public EntityLink() {
        this(0);
    }

    public EntityLink(long fromEntityUniqueId) {
        this(fromEntityUniqueId, 0);
    }

    public EntityLink(long fromEntityUniqueId, long toEntityUniqueId) {
        this(fromEntityUniqueId, toEntityUniqueId, TYPE_REMOVE);
    }

    public EntityLink(long fromEntityUniqueId, long toEntityUniqueId, byte type) {
        this(fromEntityUniqueId, toEntityUniqueId, type, false);
    }

    public EntityLink(long fromEntityUniqueId, long toEntityUniqueId, byte type, boolean immediate) {
        this.fromEntityUniqueId = fromEntityUniqueId;
        this.toEntityUniqueId = toEntityUniqueId;
        this.type = type;
        this.immediate = immediate;
    }
}
